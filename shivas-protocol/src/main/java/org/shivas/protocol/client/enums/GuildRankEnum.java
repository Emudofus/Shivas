package org.shivas.protocol.client.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 12:32
 * TODO translate
 * @author Vendethiel
 */
public enum GuildRankEnum {
    TESTING(0),
    LEADER(1),
    SECOND_LEADER(2),
    Tresorier(3),
    Protecteur(4),
    Artisan(5),
    Reserviste(6),
    Larbin(7),
    Gardien(8),
    Eclaireur(9),
    Espion(10),
    Diplomate(11),
    Secretaire(12),
    Penitent(13),
    Boulet(14),
    Deserteur(15),
    Bourreau(16),
    Apprenti(17),
    Marchand(18),
    Eleveur(19),
    Recruteur(20),
    Guide(21),
    Mentor(22),
    Elu(23),
    Conseiller(24),
    Muse(25),
    Gouverneur(26),
    Assassin(27),
    Initie(28),
    Voleur(29),
    ChercheurDeTresors(30),
    Braconnier(31),
    Traitre(32),
    TueurDeFamiliers(33),
    Mascotte(34),
    TueurDePercepteur(35);

    private int value;
    GuildRankEnum(int value) { this.value = value; }
    public int value() { return value; }

    public static GuildRankEnum valueOf(int value) {
        for (GuildRankEnum rank : values()) if (rank.value() == value) return rank;
        return null;
    }
}
