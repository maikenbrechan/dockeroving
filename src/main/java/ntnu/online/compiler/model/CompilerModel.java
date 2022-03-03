package ntnu.online.compiler.model;

import lombok.*;
import javax.persistence.*;
import java.io.File;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files")

public class CompilerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;

    private StringBuilder output;

}
