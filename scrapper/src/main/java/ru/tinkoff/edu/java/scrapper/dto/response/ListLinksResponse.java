package ru.tinkoff.edu.java.scrapper.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListLinksResponse {
    private int size;
    private LinkResponse[] links;

    public ListLinksResponse(List<TrackedLink> links) {
        this.links =  links.stream().map(LinkResponse::new).toArray(LinkResponse[]::new);
        size = links.size();
    }

}
