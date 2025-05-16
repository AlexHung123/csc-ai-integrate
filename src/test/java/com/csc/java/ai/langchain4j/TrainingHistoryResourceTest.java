package com.csc.java.ai.langchain4j;


import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.entity.CIDProfileSnapshots;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;
import com.csc.java.ai.langchain4j.service.CIDProfileSnapshotsService;
import com.csc.java.ai.langchain4j.service.TrainingHistoryResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TrainingHistoryResourceTest {

    @Autowired
    private TrainingHistoryResourceService trainingHistoryResourceService;

    @Autowired
    private CIDProfileSnapshotsService cidProfileSnapshotsService;


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
}
