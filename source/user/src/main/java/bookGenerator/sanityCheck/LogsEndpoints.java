package bookGenerator.sanityCheck;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import bookGenerator._global.logger.CustomLogger;
import bookGenerator._global.logger.CustomLoggerType;

// 현재 저장된 로그들 중에서 일부분을 간편하게 가져오기 위해서
@RestController
@RequestMapping("/sanityCheck")
public class LogsEndpoints {
    private final String logFilePath = "./logs/logback.log";


    @Data
    @ToString
    private class LogsReqDto {
        private int lineLength = 10;
        private String regFilter = "";
    }

    @Getter
    @ToString
    public class LogsResDto {
        private final List<String> logs;

        public LogsResDto(List<String> logs) {
            this.logs = logs;
        }
    }

    // 현재 저장된 로그들 중에서 일부분을 간편하게 가져오기 위해서
    @GetMapping("/logs")
    public ResponseEntity<LogsResDto> logs(@ModelAttribute LogsReqDto logsReqDto) {

        try {

            CustomLogger.debug(CustomLoggerType.ENTER);


            List<String> logs = this.readLogs(this.logFilePath, logsReqDto.getRegFilter());
            LogsResDto logsResDto = new LogsResDto(logs.subList(Math.max(logs.size()-logsReqDto.getLineLength(), 0), logs.size()));


            CustomLogger.debug(CustomLoggerType.EXIT, "", String.format("{logsSize: %d}", logsResDto.getLogs().size()));
            return ResponseEntity.ok(logsResDto);

        } catch(Exception e) {
            CustomLogger.error(e, "", String.format("{logsReqDto: %s}", logsReqDto.toString()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 로그 파일을 읽어서 필터링된 로그들을 가져오기 위해서
    private List<String> readLogs(String logFilePath, String regexString) throws FileNotFoundException {
        CustomLogger.debug(CustomLoggerType.EFFECT, "Try to read logs", String.format("{filePath: %s}", logFilePath));


        List<String> logs = new ArrayList<>();

        Scanner myReader = new Scanner(new File(logFilePath));
        while (myReader.hasNextLine())
        {
            String readLog = myReader.nextLine();
            if (regexString.isEmpty()) logs.add(readLog);
            else if(readLog.matches(regexString)) logs.add(readLog);
        }
        myReader.close();


        CustomLogger.debug(CustomLoggerType.EFFECT, "Read logs", String.format("{logsSize: %d}", logs.size()));
        return logs;
    }
}