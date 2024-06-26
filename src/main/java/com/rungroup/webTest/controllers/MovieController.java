package com.rungroup.webTest.controllers;

import com.rungroup.webTest.dtos.MovieDto;
import com.rungroup.webTest.models.Movie;
import com.rungroup.webTest.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class MovieController {
    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String listMovies(Model model){
        List<MovieDto> movies = movieService.findAllMovies();
        model.addAttribute("movies", movies);
        return "movies-list";
    }
    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable("id") long id, Model model) {
        MovieDto movieDto = movieService.findMovieById(id);
        model.addAttribute("movie", movieDto);
        model.addAttribute("genres" , movieDto.getGenres());
        return "movie-detail";
    }
    @GetMapping("/movies/new")
    public String createMovieForm(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie",movie);
        return "movie-create";
    }

    @PostMapping("/movies/new")
    public String saveMovie(@Valid @ModelAttribute("movie") MovieDto movieDto,
                            BindingResult result, Model model){
        if ( result.hasErrors()){
            model.addAttribute("movie",movieDto);
            return  "movie-create";
        }
        movieService.saveMovie(movieDto);
        return "redirect:/movies";
    }

    @GetMapping("/movies/{id}/edit")
    public String editMovieForm(@PathVariable("id") long movieId , Model model){
        MovieDto movie = movieService.findMovieById(movieId);
        model.addAttribute("movie" , movie);
        return  "movies-edit";
    }
    @PostMapping("/movies/{id}/edit")
    public String updateMovie(@PathVariable("id") long movieId ,
                              @Valid @ModelAttribute("movie") MovieDto movie,
                              BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("movie", movie);
            return "movies-edit";
        }
        movie.setId(movieId);
        movieService.updateMovie(movie);
        return "redirect:/movies";
    }
    @GetMapping ("/movies/{id}/delete")
    public String deleteMovie (@PathVariable("id") long id)
    {
        movieService.delete(id);
        return "redirect:/movies";
    }
    @GetMapping ("/movies/search")
    public String searchMovie(@RequestParam ( value = "query") String query, Model model){
        List<MovieDto> movies = movieService.searchMovies(query);
        model.addAttribute("movies", movies);
        return "movies-list";
    }
}
