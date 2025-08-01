package com.quantumsoft.hrms.Human_Resource_Website.config;

import com.quantumsoft.hrms.Human_Resource_Website.security.CustomUserDetailsService;
import com.quantumsoft.hrms.Human_Resource_Website.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs.yaml"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ enables CORS using @Bean CorsConfigurationSource
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth->auth

                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()

                                .requestMatchers("/api/admin/**").permitAll()

                                .requestMatchers("/api/users/login").permitAll()
                                .requestMatchers("/api/users/logout").permitAll()
                                .requestMatchers("/api/users/resetPwd").permitAll()
                                .requestMatchers("/api/users/forgotPwd").permitAll()
                                .requestMatchers("/api/users/role/**").permitAll()
                                .requestMatchers("/api/users/create").hasAnyRole("ADMIN","HR")
                                .requestMatchers("/api/users/status/**").hasAnyRole("ADMIN","HR")
                                .requestMatchers("/api/users/role_change/**").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/users/all").hasAnyRole("ADMIN","HR")


                                .requestMatchers("/api/employees/create").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employees/update/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employees/delete/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employees/get/**").hasAnyRole("HR", "ADMIN", "MANAGER", "EMPLOYEE","FINANCE")
                                .requestMatchers("/api/employees/all").hasAnyRole( "HR", "ADMIN", "MANAGER", "EMPLOYEE","FINANCE")
                                .requestMatchers("/api/employee/by-role/**").hasAnyRole("ADMIN", "HR")

                                .requestMatchers(HttpMethod.POST,"/api/departments/create").hasAnyRole("HR", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/departments/update/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers( HttpMethod.GET,"/api/departments/all").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/departments/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/api/departments/delete/**").hasAnyRole("HR", "ADMIN")

                                .requestMatchers("/api/leaves/apply").hasAnyRole("ADMIN", "HR", "MANAGER", "EMPLOYEE")
                                .requestMatchers("/api/leaves/all_leave").hasAnyRole("ADMIN", "HR", "MANAGER", "EMPLOYEE")
                                .requestMatchers("/api/leaves/{id}").hasAnyRole("ADMIN", "HR", "MANAGER", "EMPLOYEE")
                                .requestMatchers("/api/leaves/approve/**", "/api/leave/reject/**").hasAnyRole("ADMIN", "HR", "MANAGER")

                                .requestMatchers("/api/leave-types/get/**").hasAnyRole("HR", "ADMIN","EMPOLYEE", "MANAGER")
                                .requestMatchers("/api/leave-types/create").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/leave-types/all").hasAnyRole("HR","ADMIN", "EMPLOYEE","MANAGER")
                                .requestMatchers("/api/leave-types/update/**").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/leave-types/delete/**").hasAnyRole("HR","ADMIN")

                                .requestMatchers("/api/optional-holidays/create").hasAnyRole("HR","ADMIN", "MANAGER")
                                .requestMatchers("/api/optional-holidays/**").hasAnyRole("HR","ADMIN", "MANAGER")
                                .requestMatchers("/api/optional-holidays/search/**").hasAnyRole("HR", "ADMIN","EMPOLYEE", "MANAGER")
                                .requestMatchers("/api/optional-holidays/all").hasAnyRole("HR", "ADMIN","EMPOLYEE", "MANAGER")
                                .requestMatchers("/api/optional-holidays/delete/**").hasAnyRole("HR", "ADMIN","EMPOLYEE", "MANAGER")

                                .requestMatchers("/api/attendance/clock-in", "/api/attendance/clock-out").hasAnyRole("EMPLOYEE","MANAGER", "HR")
                                .requestMatchers("/api/attendance/regularize/approve/{attendanceId}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/attendance/regularize/reject/{attendanceId}").hasAnyRole("HR","ADMIN")
                        .requestMatchers("/ap/attendance/regularize/requests/all").hasAnyRole("HR","EMPLOYEE")
                                .requestMatchers("/api/attendance/regularize/request").hasAnyRole("EMPLOYEE","MANAGER")
                                .requestMatchers("/api/attendance/report/csv").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/attendance/report/pdf").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/attendance//status/{empId}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/attendance/{attendanceId}").permitAll()

                                .requestMatchers("/api/compliances/create").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliances/update/{id}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliances/all").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliances/get/{id}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliances/delete/{id}").hasAnyRole("HR","ADMIN")

                                .requestMatchers("/api/compliances/{id}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliance-records").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliance-records/{period}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/compliance-records/{id}").hasAnyRole("HR","ADMIN")

                                .requestMatchers("/api/benefits/create").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/benefits/update/{id}").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/benefits/all").hasAnyRole("HR", "ADMIN","EMPLOYEE", "MANAGER")
                                .requestMatchers("/api/benefits/delete/{id}").hasAnyRole("HR", "ADMIN")

                                .requestMatchers("/api/projects/create").hasAnyRole("HR","ADMIN","MANAGER")
                                .requestMatchers("/api/projects/update/{id}").hasAnyRole("HR","ADMIN", "MANAGER")
                                .requestMatchers("/api/projects/all").hasAnyRole("HR", "ADMIN", "MANAGER")
                                .requestMatchers("/api/projects/delete/{id}").hasAnyRole("HR", "ADMIN", "MANAGER")


                                 .requestMatchers("/api/employee-benefits/assign").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employee-benefits/update/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employee-benefits/employee/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employee-benefits/delete/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employee-benefits/soft-delete/**").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/employee-benefits/employee/{id}").hasAnyRole("HR", "ADMIN","EMPLOYEE")

                                .requestMatchers("/api/salary-structures/create/{employeeId}").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/salary-structures/all").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/salary-structures/employee/{employeeId}").hasAnyRole("HR", "ADMIN","EMPLOYEE")
                                .requestMatchers("/api/salary-structures/employee/{employeeId}/current").hasAnyRole("HR", "ADMIN","EMPLOYEE")
                                .requestMatchers("/api/salary-structures/{id}").hasAnyRole("ADMIN", "HR")

                                .requestMatchers("/api/payroll/generate").hasAnyRole("HR", "ADMIN")
                                .requestMatchers("/api/payroll/download/{id}").hasAnyRole("HR", "ADMIN", "EMPLOYEE", "MANAGER")
                                .requestMatchers("/api/payroll/view/{id}").hasAnyRole("HR", "ADMIN", "EMPLOYEE", "MANAGER")

                                .requestMatchers("/api/banking/create").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/banking/{employeeId}").hasAnyRole("HR","ADMIN","EMPLOYEE")
                                .requestMatchers("/api/banking/all").hasAnyRole("HR","ADMIN")
                                .requestMatchers("/api/banking/delete").hasAnyRole("HR","ADMIN")

                        .requestMatchers("/api/**").authenticated()//frontend change
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // or specific origins
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
