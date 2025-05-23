package com.csc.java.ai.langchain4j.tools;

import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.service.CIDProfileSnapshotsService;
import com.csc.java.ai.langchain4j.service.TrainingHistoryResourceService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GenerateTraineeProfileTools {

    @Autowired
    private TrainingHistoryResourceService trainingHistoryResourceService;

    @Autowired
    private CIDProfileSnapshotsService cidProfileSnapshotsService;

    public String generateTraineeProfileEnglishCoverDescription(@P(value = "College ID") String collegeId) {
        List<TraineeProfileDTO> results1 = cidProfileSnapshotsService.getTraineeProfileData(collegeId);
        List<TrainingHistoryDTO> results2 = trainingHistoryResourceService.selectTrainingHistoryDataByCollegeId(collegeId);

        String profileData = formatProfileData(results1);
        String trainingHistory = formatTrainingHistory(results2);

        StringBuilder sb = new StringBuilder();
        sb.append(profileData).append("\n").append(trainingHistory).append("\n");

        System.out.println(sb);
        return sb.toString();
    }


//    @Tool(name = "Creating formal English descriptions for trainee profiles")
//    public String generateTraineeProfileEnglishCoverDescription(@P(value = "College ID") String collegeId) {
//        List<TraineeProfileDTO> results1 = cidProfileSnapshotsService.getTraineeProfileData(collegeId);
//        List<TrainingHistoryDTO> results2 = trainingHistoryResourceService.selectTrainingHistoryDataByCollegeId(collegeId);
//
//        String profileData = formatProfileData(results1);
//        String trainingHistory = formatTrainingHistory(results2);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(profileData).append("\n").append(trainingHistory).append("\n");
//
//        System.out.println(sb);
//        return sb.toString();
//    }

    private String formatProfileData(List<TraineeProfileDTO> profiles) {
        if (profiles == null || profiles.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("no_think Posting Change:\n");
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
        sb.append("Training History:\n");

        for (TrainingHistoryDTO dto : histories) {
            String keyword = extractKeyword(dto.getCourseName());

            // 截取前10位作为日期（YYYY-MM-DD）
            String dateStr = dto.getCreatedTime().substring(0, 10);

            sb.append(String.format("Participated in [%s] in %s.\n",
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
