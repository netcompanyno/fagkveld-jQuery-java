var Filmer = {};

Filmer.lastFilmer = function(tabellId, nullstillId, skjemaId, dialogId) {
  Filmer.hentFilmerOgLeggTilRader(tabellId);
  Filmer.aktiverAutocomplete();
  Filmer.aktiverDatovelger();
  Filmer.aktiverLagringAvNyFilm(skjemaId, tabellId);
  Filmer.aktiverNullstillingAvFilmer(nullstillId, tabellId);
  Filmer.aktiverSorterbareTabeller();
  Filmer.aktiverNyttBildeDialog(dialogId, tabellId);
  Filmer.aktiverNyttBildeKnapper(dialogId);
  Filmer.aktiverTextAreaResizer();
  Filmer.aktiverFilterBoks();
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
           "<td>" + film.kommentar + "</td>" +
           "<td>" + Filmer.leggTilBilder(film) + "<input type='button' class='nyttBildeKnapp' value='+' data-film-id='" + film.id + "'/></td>" +
         "</tr>";
};

Filmer.leggTilBilder = function(film) {
  var bilder = "";

  jQuery.each(film.bilder, function(i, bilde) {
    bilder += "<a class='lightbox' href='" + bilde + "'><img class='thumbnail' src='" + bilde + "'/></a>";
  });

  return bilder;
};

Filmer.leggTilFilmer = function(filmer, tabellId) {
  var tabellRader = jQuery(tabellId + " tbody");
  tabellRader.empty();

  jQuery.each(filmer, function(i, film) {
    tabellRader.append(Filmer.lagTabellRad(film));
  });

  Filmer.oppdaterSorterbareTabeller();
  Filmer.giFokusTilForsteElement();
  Filmer.aktiverLightBox();
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
    var lagreKnapp = jQuery(this);

    if (!Filmer.harValideringsfeil(skjema)) {
      lagreKnapp.attr("disabled", "disabled");

      jQuery.post(
          skjema.attr("action"),
          skjema.serialize(),
          function(filmer) {
            Filmer.leggTilFilmer(filmer, tabellId);
            skjema.find("input[type=reset]").click();
            lagreKnapp.removeAttr("disabled");
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

Filmer.aktiverSorterbareTabeller = function () {
  jQuery("table").tablesorter({widgets: ['zebra']});
};

Filmer.oppdaterSorterbareTabeller = function() {
  jQuery("table").trigger("update");
  jQuery("table").trigger("applyWidgets");
};

Filmer.giFokusTilForsteElement = function() {
  jQuery("input[type=text]:first").focus();
};

Filmer.aktiverNyttBildeDialog = function(dialogId, tabellId) {
  var dialog = jQuery(dialogId);

  dialog.dialog({
    autoOpen: false,
    height: 300,
    width: 350,
    modal: true,
    buttons: {
      "Lagre": function() {
        jQuery.post(
            dialog.find("form").attr("action"),
            dialog.find("form").serialize(),
            function(filmer) {
              Filmer.leggTilFilmer(filmer, tabellId);
            },
            "json"
        );
        jQuery(this).dialog("close");
      },
      "Avbryt": function() {
        jQuery(this).dialog("close");
      }
    }
  });
};

Filmer.aktiverNyttBildeKnapper = function(dialogId) {
  jQuery(".nyttBildeKnapp").live("click", function() {
    jQuery(dialogId).dialog("open");
    jQuery(dialogId).find("input[type=hidden]").val(jQuery(this).data("filmId"));
    jQuery(dialogId).find("input[type=text]").val("");
  });
};

Filmer.aktiverTextAreaResizer = function() {
  jQuery("textarea").TextAreaResizer();
};

Filmer.aktiverLightBox = function() {
	jQuery('a.lightbox').lightBox({fixedNavigation:true});
};

Filmer.aktiverFilterBoks = function() {
  jQuery(".tablefilter").keyup(function() {
    var tabellId = jQuery(".tablefilter").data("tabellId");
    jQuery.uiTableFilter(jQuery(tabellId), this.value);
  });
};
