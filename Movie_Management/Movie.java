package Movie_Management;

import Database.DatabaseUtils;
import Driver.DatabaseOperations;
import Driver.DateTime;
import Driver.Name;
import Driver.SystemClass;
import Genre_Management.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Movie implements DatabaseOperations {
    private int movieID;
    private Genre genre;
    private Name mvName;
    private DateTime releaseDate;
    private int duration;
    private String language;
    private String director;
    private String writter;
    private String starring;
    private String musicProvider;
    private String country;
    private String metaDescription;
    private double basicTicketPrice;
    private int status;

    public Movie(){
    }

    public Movie(int movieID, Genre genre, Name mvName, DateTime releaseDate, int duration, String language, String director, String writter, String starring, String musicProvider, String country, String metaDescription, double basicTicketPrice) {
        this.movieID = movieID;
        this.genre = genre;
        this.mvName = mvName;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.language = language;
        this.director = director;
        this.writter = writter;
        this.starring = starring;
        this.musicProvider = musicProvider;
        this.country = country;
        this.metaDescription = metaDescription;
        this.basicTicketPrice = basicTicketPrice;
    }

    // Method
    public void viewMovieDetails() throws SQLException {
        ResultSet result = null;

        try {
            Object[] params = {genre.getGenreID()};
            result = DatabaseUtils.selectQueryById("genre_name", "genre", "genre_id = ?", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("\nMovie Detail:\n");
        System.out.println("Movie Name: " + mvName.getName());
        if (result.next()) {
            System.out.println("Genre: " + result.getString("genre_name"));
        }
        System.out.println("Release Date: " + releaseDate.getDate());
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Language: " + language);
        System.out.println("Director: " + director);
        System.out.println("Writter: " + writter);
        System.out.println("Starring: " + starring);
        System.out.println("Music Producer: " + musicProvider);
        System.out.println("Country: " + country);
        System.out.printf("%s %.2f\n", "Basic Ticket Price:", basicTicketPrice);
        System.out.println("\nSynopsis:\n" + metaDescription);
    }

    public boolean add() throws SQLException {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `movie`(`genre_id`, `mv_name`, `release_date`, `duration`, `lang`, `director`, `writter`, `starring`, `music`, `country`,`meta_description`, `basic_TicketPrice`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {genre.getGenreID(), mvName.getName(), String.valueOf(releaseDate.getDate()), duration, language, director, writter, starring, musicProvider, country, metaDescription, basicTicketPrice};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nMovie successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong!");
            return false;
        }
    }

    public int modifyMovieDetail(Scanner sc) throws SQLException {
        boolean error = true;

        do {
            try {
                Object[] params = {genre.getGenreID()};
                ResultSet result = DatabaseUtils.selectQueryById("genre_name", "genre", "genre_id = ?", params);

                try {
                    int count = 1;
                    System.out.printf("\nMovie Detail:\n");
                    System.out.println(count + ". Movie Name: " + mvName.getName());
                    count++;
                    if (result.next()) {
                        System.out.println(count + ". Genre: " + result.getString("genre_name"));
                        count++;
                    }
                    System.out.println(count + ". Release Date: " + releaseDate.getDate());
                    count++;
                    System.out.println(count + ". Duration: " + duration + " minutes");
                    count++;
                    System.out.println(count + ". Language: " + language);
                    count++;
                    System.out.println(count + ". Director: " + director);
                    count++;
                    System.out.println(count + ". Writter: " + writter);
                    count++;
                    System.out.println(count + ". Starring: " + starring);
                    count++;
                    System.out.println(count + ". Music Producer: " + musicProvider);
                    count++;
                    System.out.println(count + ". Country: " + country);
                    count++;
                    System.out.printf("%d. %s: %.2f\n", count, "Basic Ticket Price", basicTicketPrice);
                    count++;
                    System.out.println("\n" + count + ". Synopsis:\n" + metaDescription + "\n");

                    System.out.print("\nEnter the serial number of the movie information you want to change (0 - Stop): ");
                    int serialNum = sc.nextInt();
                    sc.nextLine();

                    if (serialNum < 0 || serialNum > count) {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                        error = true;
                    } else {
                        return serialNum;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                    error = true;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } while (error);
        return 0;
    }

    public boolean modify() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `movie` SET `genre_id`= ?, `mv_name`= ?," +
                    "`release_date`= ?,`duration`= ?,`lang`= ?," +
                    "`director`= ?,`writter`= ?,`starring`= ?,`music`= ?," +
                    "`country`= ?,`meta_description`= ?, `basic_TicketPrice`= ? WHERE `movie_id` = ?";
            Object[] params = {genre.getGenreID(), mvName.getName(), String.valueOf(releaseDate.getDate()), duration, language, director, writter, starring, musicProvider, country, metaDescription, basicTicketPrice, movieID};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public boolean delete() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {movieID};
            rowAffected = DatabaseUtils.deleteQueryById("movie", "movie_status", "movie_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe movie has been deleted.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setGenre(Genre genre){
        this.genre = genre;
    }

    public void setMvName(Name mvName) {
        this.mvName = mvName;
    }

    public void setReleaseDate(DateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWritter(String writter) {
        this.writter = writter;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public void setMusicProvider(String musicProvider) {
        this.musicProvider = musicProvider;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public void setBasicTicketPrice(double basicTicketPrice) {
        this.basicTicketPrice = basicTicketPrice;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMovieID() {
        return movieID;
    }

    public Genre getGenre(){
        return genre;
    }

    public Name getMvName() {
        return mvName;
    }

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getLanguage() {
        return language;
    }

    public String getDirector() {
        return director;
    }

    public String getWritter() {
        return writter;
    }

    public String getStarring() {
        return starring;
    }

    public String getMusicProvider() {
        return musicProvider;
    }

    public String getCountry() {
        return country;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public double getBasicTicketPrice() {
        return basicTicketPrice;
    }

    public int getStatus() {
        return status;
    }
}