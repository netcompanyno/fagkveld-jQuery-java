var Filmer = {};

Filmer.lastFilmer = function(tabellId, nullstillId, skjemaId) {
  Filmer.hentFilmerOgLeggTilRader(tabellId);
  Filmer.aktiverAutocomplete();
  Filmer.aktiverDatovelger();
  Filmer.aktiverLagringAvNyFilm(skjemaId, tabellId);
  Filmer.aktiverNullstillingAvFilmer(nullstillId, tabellId);
};

Filmer.hentFilmerOgLeggTilRader = function(tabellId) {
  var url = jQuery(tabellId).data("url");

  jQuery.getJSON(url, function(filmer) {
    Filmer.leggTilFilmer(filmer, tabellId);
  });
};

Filmer.konverterDato = function(film) {
  var dato = new Date(film.lanseringsdato);
  return jQuery.datepicker.formatDate('dd.mm.yy', dato);
};

Filmer.lagTabellRad = function(film) {
  return "<tr>" +
           "<td>" + film.id + "</td>" +
           "<td>" + film.tittel + "</td>" +
           "<td>" + film.regissor + "</td>" +
           "<td>" + Filmer.konverterDato(film) + "</td>" +
           "<td>" + film.bilder + "</td>" +
         "</tr>";
};

Filmer.leggTilFilmer = function(filmer, tabellId) {
  var tabellRader = jQuery(tabellId + " tbody");
  tabellRader.empty();

  jQuery.each(filmer, function(i, film) {
    tabellRader.append(Filmer.lagTabellRad(film));
  });

  jQuery(tabellId).trigger("update"); // TODO
  jQuery(tabellId).trigger("applyWidgets"); // TODO
  jQuery("input[type=text]:first").focus(); // TODO
};

Filmer.aktiverDatovelger = function() {
  jQuery(".date").datepicker();
};

Filmer.aktiverAutocomplete = function() {
  jQuery(".autocomplete").each(function() {
    jQuery(this).autocomplete({
      source: jQuery(this).data("url"),
      minLength: 2
    });
  });
};

Filmer.aktiverLagringAvNyFilm = function(skjemaId, tabellId) {
  var skjema = jQuery(skjemaId);

  skjema.find("input[type=submit]").click(function(event) {
    event.preventDefault();

    if (!Filmer.harValideringsfeil(skjema)) {
      jQuery.post(
          skjema.attr("action"),
          skjema.serialize(),
          function(filmer) {
            Filmer.leggTilFilmer(filmer, tabellId);
            skjema.find("input[type=reset]").click();
          },
          "json"
      );
    }
  });
};

Filmer.harValideringsfeil = function(skjema) {
  return !skjema.validate().form();
};

Filmer.aktiverNullstillingAvFilmer = function(nullstillId, tabellId) {
  jQuery(nullstillId).click(function(e) {
    jQuery.post(
        jQuery(this).data("url"),
        function(filmer) {
          Filmer.leggTilFilmer(filmer, tabellId);
        },
        "json"
    );
  });
};
