package com.exercise.firstExercise.services;

import com.exercise.firstExercise.dto.QuestionDTO;
import com.exercise.firstExercise.dto.ResultDTO;
import com.exercise.firstExercise.models.Disaster;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jFuzzyLogic.FIS;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ResultService {
    private final List<QuestionDTO> questions;
    private final Map<Disaster,Double> risks;

    public ResultService(List<QuestionDTO> questions, Map<Disaster, Double> risks) {
        this.questions = questions;
        this.risks = risks;
    }

    private void LoadQuestionsFromTable(String filePath){
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            int typeColIndex = -1;
            int titleColIndex = -1;
            int optionsColIndex = -1;

            for (Cell cell : headerRow) {
                String colName = cell.getStringCellValue().trim().toLowerCase();
                if (colName.contains("dontinsert")) continue;
                if (colName.equals("question_type")) typeColIndex = cell.getColumnIndex();
                if (colName.equals("question_title")) titleColIndex = cell.getColumnIndex();
                if(colName.equals("options")) optionsColIndex = cell.getColumnIndex();
            }

            if (typeColIndex == -1 || titleColIndex == -1 || optionsColIndex == -1) {
                throw new RuntimeException("Missing 'question_type' or 'question_title' column.");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell typeCell = row.getCell(typeColIndex);
                Cell titleCell = row.getCell(titleColIndex);
                Cell optionsCell = row.getCell(optionsColIndex);

                if (typeCell == null || titleCell == null || optionsCell == null) continue;

                Disaster type = Disaster.valueOf(typeCell.toString().trim());
                String title = titleCell.toString().trim();
                int options = (int)optionsCell.getNumericCellValue();
                questions.add(new QuestionDTO(title, type, options));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultDTO GetAllRisks(Map<String, Integer> answers){
        LoadQuestionsFromTable("C:/Users/Acer/Desktop/questions_reading.xlsx");
        for(Disaster disaster: Disaster.values()){
            if(disaster == Disaster.GRADUSHKA || disaster == Disaster.SILNI_VETROVE)
            {
                risks.put(disaster, GetTheRisk(disaster, answers));
            }
            else
                risks.put(disaster, GetTheRiskLongerVersion(disaster, answers));
        }
        ResultDTO res = new ResultDTO(risks);
        return res;
    }

    private Double GetTheRisk(Disaster type, Map<String, Integer> answers){
        String pathToFCL = type.toString()+".fcl";
        InputStream inputStream = ResultService.class.getClassLoader().getResourceAsStream(pathToFCL);

        if (inputStream == null) {
            System.out.println("FCL file not found in resources.");
            return null;
        }

        FIS fis = FIS.load(inputStream, true);

        if (fis == null) {
            System.out.println("Error loading FCL file.");
            return null;
        }

        for(QuestionDTO question: questions){
            if(question.disaster().equals(type)){
                Integer valueOfTheAnswer = answers.entrySet()
                        .stream()
                        .filter(answer -> answer.getKey().equals(question.title()))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(null);
                int transformedValue = FromPositionToNumber(question.options(), valueOfTheAnswer);
                fis.setVariable(question.title(),transformedValue);
            }
        }

        fis.evaluate();

        return 100.0-fis.getVariable(type.toString()+"_risk").getValue();
    }

    private Double GetTheRiskLongerVersion(Disaster type, Map<String, Integer> answers){
        String pathToFCL = "disaster_long.fcl";
        InputStream inputStream = ResultService.class.getClassLoader().getResourceAsStream(pathToFCL);

        if (inputStream == null) {
            System.out.println("FCL file not found in resources.");
            return null;
        }

        FIS fis = FIS.load(inputStream, true);

        if (fis == null) {
            System.out.println("Error loading FCL file.");
            return null;
        }

        List<Integer> answersToTheFcl = new ArrayList<>();
        for(QuestionDTO question: questions){
            if(question.disaster().equals(type)){
                Integer position = answers.get(question.title());
                if (position != null) {
                    int transformedValue = FromPositionToNumber(question.options(), position);
                    answersToTheFcl.add(transformedValue);
                }
            }
        }

        if(type == Disaster.PROLIVEN_DAJD_NAVODNENIE){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.15) +
                            (answersToTheFcl.get(1) * 0.10) +
                            (answersToTheFcl.get(2) * 0.10) +
                            (answersToTheFcl.get(3) * 0.12) +
                            (answersToTheFcl.get(4) * 0.12) +
                            (answersToTheFcl.get(5) * 0.08) +
                            (answersToTheFcl.get(6) * 0.08) +
                            (answersToTheFcl.get(7) * 0.10) +
                            (answersToTheFcl.get(8) * 0.08) +
                            (answersToTheFcl.get(9) * 0.07);
            fis.setVariable("weighted_risk", weighted_risk);
        }
        else if(type == Disaster.OGAN){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.25) +
                            (answersToTheFcl.get(1) * 0.2) +
                            (answersToTheFcl.get(2) * 0.2) +
                            (answersToTheFcl.get(3) * 0.15) +
                            (answersToTheFcl.get(4) * 0.1) +
                            (answersToTheFcl.get(5) * 0.1);
            fis.setVariable("weighted_risk", weighted_risk);
        }
        else if(type == Disaster.OSLANQVANE){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.2) +
                            (answersToTheFcl.get(1) * 0.15) +
                            (answersToTheFcl.get(2) * 0.15) +
                            (answersToTheFcl.get(3) * 0.1) +
                            (answersToTheFcl.get(4) * 0.1) +
                            (answersToTheFcl.get(5) * 0.1) +
                            (answersToTheFcl.get(6) * 0.1) +
                            (answersToTheFcl.get(7) * 0.05) +
                            (answersToTheFcl.get(8) * 0.05);
            fis.setVariable("weighted_risk", weighted_risk);
        }
        else if(type == Disaster.KISHA){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.2) +
                            (answersToTheFcl.get(1) * 0.15) +
                            (answersToTheFcl.get(2) * 0.15) +
                            (answersToTheFcl.get(3) * 0.1) +
                            (answersToTheFcl.get(4) * 0.1) +
                            (answersToTheFcl.get(5) * 0.1) +
                            (answersToTheFcl.get(6) * 0.05) +
                            (answersToTheFcl.get(7) * 0.05) +
                            (answersToTheFcl.get(8) * 0.1);
            fis.setVariable("weighted_risk", weighted_risk);
        }
        else if(type == Disaster.IZMRAZVANE){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.2) +
                            (answersToTheFcl.get(1) * 0.15) +
                            (answersToTheFcl.get(2) * 0.1) +
                            (answersToTheFcl.get(3) * 0.1) +
                            (answersToTheFcl.get(4) * 0.1) +
                            (answersToTheFcl.get(5) * 0.05) +
                            (answersToTheFcl.get(6) * 0.1) +
                            (answersToTheFcl.get(7) * 0.05) +
                            (answersToTheFcl.get(8) * 0.05) +
                            (answersToTheFcl.get(9) * 0.1);
            fis.setVariable("weighted_risk", weighted_risk);
        }
        else if(type == Disaster.IZTEGLQNE){
            double weighted_risk =
                    (answersToTheFcl.get(0) * 0.2) +
                            (answersToTheFcl.get(1) * 0.15) +
                            (answersToTheFcl.get(2) * 0.1) +
                            (answersToTheFcl.get(3) * 0.1) +
                            (answersToTheFcl.get(4) * 0.1) +
                            (answersToTheFcl.get(5) * 0.05) +
                            (answersToTheFcl.get(6) * 0.1) +
                            (answersToTheFcl.get(7) * 0.1) +
                            (answersToTheFcl.get(8) * 0.05) +
                            (answersToTheFcl.get(9) * 0.05);
            fis.setVariable("weighted_risk", weighted_risk);
        }

        fis.evaluate();

        return 100.0-fis.getVariable("disaster_risk").getValue();
    }

    private int FromPositionToNumber(int questionOptionsNumber, Integer ans){
        switch(questionOptionsNumber) {
            case 2:
                if(ans == 1)
                    return 25;
                else
                    return 75;
            case 3:
                if(ans == 1)
                    return 20;
                else if(ans == 2)
                    return 50;
                else
                    return 80;
            case 4:
                if(ans == 1)
                    return 15;
                else if(ans == 2)
                    return 40;
                else if(ans == 3)
                    return 65;
                else
                    return 90;
        }
        return 0;
    }

}
