package no.mesan.js.kontroller;

import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author Christian Ihle
 */
public class Film {

    private Integer id;
    private String tittel;
    private String regissor;
    private Date lanseringsdato;
    private String kommentar;
    private List<String> bilder;

    public Film() {

    }

    public Film(final Integer id, final String tittel, final String regissor, final Date lanseringsdato, final String kommentar, final List<String> bilder) {
        this.id = id;
        this.tittel = tittel;
        this.regissor = regissor;
        this.lanseringsdato = lanseringsdato;
        this.kommentar = kommentar;
        this.bilder = bilder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(final String tittel) {
        this.tittel = tittel;
    }

    public String getRegissor() {
        return regissor;
    }

    public void setRegissor(final String regissor) {
        this.regissor = regissor;
    }

    public Date getLanseringsdato() {
        return lanseringsdato;
    }

    public void setLanseringsdato(final Date lanseringsdato) {
        this.lanseringsdato = lanseringsdato;
    }

    public List<String> getBilder() {
        return bilder;
    }

    public void setBilder(final List<String> bilder) {
        this.bilder = bilder;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(final String kommentar) {
        this.kommentar = kommentar;
    }
}
