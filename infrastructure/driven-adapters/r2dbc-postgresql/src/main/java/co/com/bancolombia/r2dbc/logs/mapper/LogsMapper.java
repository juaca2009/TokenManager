package co.com.bancolombia.r2dbc.logs.mapper;

import co.com.bancolombia.model.logs.Logs;
import co.com.bancolombia.r2dbc.logs.LogsData;

public class LogsMapper {

    public static LogsData toData(Logs logs) {
        return LogsData.builder()
                .id(logs.getId())
                .userId(logs.getUserId())
                .action(logs.getAction())
                .ipAddress(logs.getIpAdress())
                .dateLog(logs.getDate())
                .build();
    }
}
