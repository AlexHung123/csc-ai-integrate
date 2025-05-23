package com.csc.java.ai.langchain4j;


import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.entity.CIDProfileSnapshots;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;
import com.csc.java.ai.langchain4j.service.CIDProfileSnapshotsService;
import com.csc.java.ai.langchain4j.service.TrainingHistoryResourceService;
import com.csc.java.ai.langchain4j.tools.GenerateTraineeProfileTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class TrainingHistoryResourceTest {

    @Autowired
    private TrainingHistoryResourceService trainingHistoryResourceService;

    @Autowired
    private CIDProfileSnapshotsService cidProfileSnapshotsService;

    @Autowired
    private GenerateTraineeProfileTools generateTraineeProfileTools;


    @Test
    void testGetMany() {
        TrainingHistoryResource trainingHistoryResource = new TrainingHistoryResource();
        trainingHistoryResource.setId(69L);

        TrainingHistoryResource one = trainingHistoryResourceService.getOne(trainingHistoryResource);
        System.out.println(one);
    }

    @Test
    void testGetMany2() {
        CIDProfileSnapshots cidProfileSnapshots = new CIDProfileSnapshots();
        cidProfileSnapshots.setId(2L);

        CIDProfileSnapshots one = cidProfileSnapshotsService.getOne(cidProfileSnapshots);
        System.out.println(one);

    }

    @Test
    void testGetMany3() {
        List<TraineeProfileDTO> traineeProfileData = cidProfileSnapshotsService.getTraineeProfileData("CS943939");
        System.out.println(traineeProfileData);
    }

    @Test
    void testGetMany4() {
        List<TrainingHistoryDTO> trainingHistoryDTOS = trainingHistoryResourceService.selectTrainingHistoryDataByCollegeId("CS943939");
        System.out.println(trainingHistoryDTOS);
    }


    @Test
    public void testGenerateTemplate() {
        List<TraineeProfileDTO> results1 = cidProfileSnapshotsService.getTraineeProfileData("CS943939");
        List<TrainingHistoryDTO> results2 = trainingHistoryResourceService.selectTrainingHistoryDataByCollegeId("CS943939");

        String profileData = formatProfileData(results1);
        String trainingHistory = formatTrainingHistory(results2);

        StringBuilder sb = new StringBuilder();
        sb.append(profileData).append("\n").append(trainingHistory).append("\n");

        System.out.println(sb);
    }


    @Test
    public void test5(){
        String s = generateTraineeProfileTools.generateTraineeProfileEnglishCoverDescription("CS943939");
        System.out.println(s);
    }

    private String formatProfileData(List<TraineeProfileDTO> profiles) {
        if (profiles == null || profiles.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("Posting change:\n");
        for (TraineeProfileDTO dto : profiles) {
            sb.append(String.format("Appointed %s at %s in %s.\n",
                    dto.getRankNameEn(),
                    dto.getDepartmentNameEn(),
                    dto.getMinCreatedTime().toString().substring(0, 10)));
        }
        return sb.toString();
    }


    private String formatTrainingHistory(List<TrainingHistoryDTO> histories) {
        if (histories == null || histories.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("Training history:\n");

        for (TrainingHistoryDTO dto : histories) {
            String keyword = extractKeyword(dto.getCourseName());

            // 截取前10位作为日期（YYYY-MM-DD）
            String dateStr = dto.getCreatedTime().substring(0, 10);

            sb.append(String.format("- Participated in [%s] in %s.\n",
                    keyword,
                    dateStr));
        }
        return sb.toString();
    }

    private String extractKeyword(String courseName) {
        if (courseName.contains("[")) {
            return courseName.replaceAll(".*\\[(.*?)\\].*", "$1");
        }
        return courseName;
    }
}
