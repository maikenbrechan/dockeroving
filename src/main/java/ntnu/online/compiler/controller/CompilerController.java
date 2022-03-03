package ntnu.online.compiler.controller;

import ntnu.online.compiler.model.CompilerModel;
import ntnu.online.compiler.repo.CompilerRepo;
import ntnu.online.compiler.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/compiler")
@CrossOrigin("http://localhost:8081")
public class CompilerController {

    @Autowired
    private CompilerRepo repo;
    @Autowired
    private CompilerService service;


    @GetMapping("/code")
    public List<CompilerModel> getcode(){
        return repo.findAll();
    }

    @PostMapping(path ="/code",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StringBuilder sendCode(@RequestBody CompilerModel compilerModel) throws IOException, InterruptedException {
        String[] words = compilerModel.getCode().split(" ");
        System.out.println("code recieved");
        String className = words[2];
        StringBuilder output = CompilerService.compileAndRun(compilerModel.getCode(), className);
        System.out.println("completed compilation");
        CompilerModel newProgram = new CompilerModel();
        newProgram.setCode(compilerModel.getCode());
        newProgram.setOutput(output);
        System.out.println("saving now");
        repo.save(newProgram);
        return output;
    }
}
