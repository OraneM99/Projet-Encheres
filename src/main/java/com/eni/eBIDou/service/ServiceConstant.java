package com.eni.eBIDou.service;

/**
 * Constantes utilisées pour les codes de réponse des services
 */
public class ServiceConstant {

    // Codes de succès
    public static final String CD_SUCCESS = "777";

    // Codes d'erreur génériques
    public static final String CD_ERR_UNKNOWN = "666";
    public static final String CD_ERR_TCH = "555";
    public static final String CD_ERR_NET = "558";
    public static final String CD_ERR_NOT_FOUND = "111";
    public static final String CD_ERR_ALREADYHERE = "333";

    // Codes spécifiques aux enchères
    public static final String CD_ERR_CREDIT_INSUFFICIENT = "222";  // Crédit insuffisant pour enchérir
    public static final String CD_ERR_ENCHERE_NON_AUTORISEE = "444"; // Enchère non autorisée (vente par proprio, etc.)
    public static final String CD_ERR_ENCHERE_TROP_BASSE = "445";   // Enchère inférieure au prix actuel
    public static final String CD_INFO_VENTE_TERMINEE = "888";      // Information : vente terminée
    public static final String CD_INFO_VENTE_NON_DEBUTEE = "999";   // Information : vente non débutée

    // Messages génériques associés
    public static final String MSG_SUCCESS = "Opération réalisée avec succès";
    public static final String MSG_ERR_UNKNOWN = "Une erreur inconnue est survenue";
    public static final String MSG_ERR_TCH = "Une erreur technique est survenue";
    public static final String MSG_ERR_NET = "Une erreur réseau est survenue";
    public static final String MSG_ERR_NOT_FOUND = "Élément non trouvé";
    public static final String MSG_ERR_ALREADYHERE = "L'élément existe déjà";

    // Messages spécifiques aux enchères
    public static final String MSG_ERR_CREDIT_INSUFFICIENT = "Crédit insuffisant pour enchérir";
    public static final String MSG_ERR_ENCHERE_NON_AUTORISEE = "Vous n'êtes pas autorisé à enchérir sur cet article";
    public static final String MSG_ERR_ENCHERE_TROP_BASSE = "Votre enchère doit être supérieure à l'enchère actuelle";
    public static final String MSG_INFO_VENTE_TERMINEE = "Cette vente est terminée";
    public static final String MSG_INFO_VENTE_NON_DEBUTEE = "Cette vente n'a pas encore débuté";
}