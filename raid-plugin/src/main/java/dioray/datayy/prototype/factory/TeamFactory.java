package dioray.datayy.prototype.factory;

public class TeamFactory {

    private static TeamFactory teamFactory;

    public static TeamFactory getInstance() {
        return teamFactory == null ? (teamFactory = new TeamFactory()) : teamFactory;
    }
}
