package me.keills.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Json {
    @NotNull
    private String method;
    @NotNull
    private String url;
    @NotNull
    private String body;
    @NotNull
    private Map<String,List<String>> headers;
    @NotNull
    private Map<String,String> parameters;
}
