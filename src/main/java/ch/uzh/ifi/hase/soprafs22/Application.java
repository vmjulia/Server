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
      double [] con = new double[2];
      con[0] = 0.0;
      con[1] = 2.0;
      solver.addConstraint(con, LpSolve.GE, 1);
      //solver.strAddConstraint("0 4 3 1", LpSolve.GE, 3);

      solver.setBinary(1, true);
      // set objective function
      double [] row = new double[2];
      row[0] = 0.0;
      row[1] = 2.0;
      solver.setObjFn(row);

      // set objective function
      //solver.strSetObjFn("2 ");
      //solver.setVerbose(LpSolve.IMPORTANT);
      solver.setMaxim();

      // solve the problem
      solver.solve();
      double[] solution = solver.getPtrPrimalSolution();
      int firstVar = 1 + solver.getNrows();

      // print solution
      System.out.println("Value of objective function: " + solver.getObjective());
      double[] var = solver.getPtrVariables();
      for (int i = 0; i < var.length; i++) {
          System.out.println("Value of var[" + i + "] = " + var[i]);
      }



     return  Double.toString(var[0])  +"obj function" + Double.toString(solver.getObjective());

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
