package no.mesan.js.kontroller;

import static org.joda.time.DateTimeConstants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author Christian Ihle
 */
@Controller
@RequestMapping("/filmer")
public class FilmKontroller {

    public static final String FILMER = "filmer";

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Film> hentFilmer(final HttpSession session) {
        final List<Film> filmerFraSesjonen = hentFilmerFraSesjonen(session);

        if (filmerFraSesjonen != null) {
            return filmerFraSesjonen;
        } else {
            final List<Film> filmer = lagFilmer();
            leggFilmerISesjonen(session, filmer);
            return filmer;
        }
    }

    @RequestMapping(value = "nullstill", method = RequestMethod.POST)
    @ResponseBody
    public List<Film> nullstill(final HttpSession session) {
        session.removeAttribute(FILMER);
        return hentFilmer(session);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<Film> leggTilFilm(final HttpSession session, final Film film) {
        return leggFilmISesjonen(session, film);
    }

    @RequestMapping(value = "regissorer", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> hentRegissorer(final HttpSession session, @RequestParam("term") final String filter) {
        final List<Film> filmer = hentFilmer(session);
        Set<String> regissorer = new HashSet<String>();

        for (final Film film : filmer) {
            if (film.getRegissor().toLowerCase().startsWith(filter.toLowerCase())) {
                regissorer.add(film.getRegissor());
            }
        }

        return regissorer;
    }

    @RequestMapping(value = "leggTilBilde", method = RequestMethod.POST)
    @ResponseBody
    public List<Film> leggTilBilde(final HttpSession session,
                                   @RequestParam("filmId") final Integer filmId,
                                   @RequestParam("url") final String url) {
        final List<Film> filmer = hentFilmer(session);
        final Film film = hentFilm(filmer, filmId);

        if (film != null) {
            film.getBilder().add(url);
        }

        return filmer;
    }

    private Film hentFilm(final List<Film> filmer, final Integer filmId) {
        for (final Film film : filmer) {
            if (film.getId().equals(filmId)) {
                return film;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Film> hentFilmerFraSesjonen(final HttpSession session) {
        return (List<Film>) session.getAttribute(FILMER);
    }

    private List<Film> lagFilmer() {
        final List<Film> filmer = new ArrayList<Film>();

        final ArrayList<String> roboCopBilder = new ArrayList<String>();
        roboCopBilder.add("http://www.otherlandtoys.co.uk/robocop800.jpg");
        roboCopBilder.add("http://www.reocities.com/Hollywood/8356/robocopCD.jpg");
        roboCopBilder.add("http://poorandweird.files.wordpress.com/2010/09/robocop-feat1.jpg");
        filmer.add(new Film(1, "RoboCop", "Paul Verhoeven",
                new DateTime().withDayOfMonth(17).withMonthOfYear(JULY).withYear(1987).toDate(),
                "Come quietly or there will be... trouble.", roboCopBilder));

        final ArrayList<String> speedBilder = new ArrayList<String>();
        speedBilder.add("http://www.movie-list.com/posters/big/zoom/speed.jpg");
        filmer.add(new Film(2, "Speed", "Jan de Bontn",
                new DateTime().withDayOfMonth(10).withMonthOfYear(JUNE).withYear(1994).toDate(),
                "Ok, Jack, this is it. Don't get dead.", speedBilder));
        return filmer;
    }


    private List<Film> leggFilmISesjonen(final HttpSession session, final Film film) {
        final List<Film> filmer = hentFilmer(session);
        film.setId(finnNesteLedigeId(filmer));
        film.setBilder(new ArrayList<String>());
        filmer.add(film);

        return filmer;
    }

    private void leggFilmerISesjonen(final HttpSession session, final List<Film> filmer) {
        session.setAttribute(FILMER, filmer);
    }

    private Integer finnNesteLedigeId(List<Film> filmer) {
        int hoyesteId = 0;

        for (final Film film : filmer) {
            if (film.getId() > hoyesteId) {
                hoyesteId = film.getId();
            }
        }

        return ++hoyesteId;
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }
}
