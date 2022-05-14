package ch.uzh.ifi.hase.soprafs22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lpsolve.*;

@RestController
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String helloWorld() throws LpSolveException {
      LpSolve solver = LpSolve.makeLp(0, 1);

      // add constraints
      //solver.strAddConstraint("3 2 2 1", LpSolve.LE, 4);
      //solver.strAddConstraint("0 4 3 1", LpSolve.GE, 3);


      // set objective function
      double []row;
      row = new double[]{10};
      solver.setObjFn(row);
      //solver.setBinary(1, false);
      //solver.setBinary(4, true);
      solver.setMaxim();


      // solve the problem
      int a = solver.solve();
      double[]sol  = solver.getPtrVariables();

      // print solution
      System.out.println("Value of objective function: " + solver.getObjective());

    return  Double.toString(sol[0])+ "res"+ Double.toString(a) +"works The application is running" + Double.toString(solver.getObjective());

  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }
    };
  }
}
