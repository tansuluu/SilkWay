package com.example.SilkWay.config;
import com.example.SilkWay.handler.CustomizeAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/adminPage","/", "/sources/**", "/jkc", "/fgrd","/hotelPage","/tourPage","/transportPage","/aboutUs").permitAll()
                .antMatchers("/login", "/image/**","/hotelInfo/**").permitAll()
                .antMatchers("/regUser", "/filterTour", "/tourInfo/**", "/bookHotel/**","/bookingHotel/**", "/findHotels", "/findTours").permitAll()
                .antMatchers("/confirm", "/buyTour/**", "/buyingTour/**").permitAll()
                .antMatchers("/superAdmin", "/regAdmin").hasAuthority("SUPER_ADMIN")
            .antMatchers("/homeAdmin",
                    "/regTour",
                    "/regCompany",
                    "/deleteUser/**",
                    "/updateTour/**",
                    "/regHotel",
                    "/deleteTour/**",
                    "/updateHotel/**",
                    "/deleteHotel/**").hasAnyAuthority("ADMIN, SUPER_ADMIN")
                .antMatchers("/admin/**").hasAuthority("SUPER_ADMIN").anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/fonts/**", "/register/**","/sass/**","/register/**","/register/css/**","/register/fonts/**","/register/images/**","/register/js/**","/register/vendor/**","/folderforadmin/**");
    }

}