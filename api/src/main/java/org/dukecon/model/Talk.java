package org.dukecon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Talk {

    private String id;
    private String track;
    private String level;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private String start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private String end;
    private String location;
    private String title;
    private List<Speaker> speakers = new ArrayList<>();
    private String _abstract;
    private String language;
    private boolean demo;

}
