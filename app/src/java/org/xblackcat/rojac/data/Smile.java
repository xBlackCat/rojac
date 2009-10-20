package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public enum Smile {
    Smile("smile", ":)", ":-)"),
    Sad("frown", ":(", ":-("),
    Wink("wink", ";)", ";-)"),
    Biggrin("biggrin", ":))", ":-))"),
    Lol("lol", ":)))", ":-)))"),
    Smirk("smirk", ":-\\"),
    Confused("confused", ":???:"),
    No("no", ":no:"),
    Up("sup", ":up:"),
    Down("down", ":down:"),
    Super("super", ":super:"),
    Shuffle("shuffle", ":shuffle:"),
    Wow("wow", ":wow:"),
    Crash("crash", ":crash:"),
    User("user", ":user:"),
    Maniac("maniac", ":maniac:"),
    DoNotKnow("xz", ":xz:"),
    Beer("beer", ":beer:");

    private static final String IMAGES_SMILES_PATH = "/images/smiles/";

    private final String[] tags;
    private final String path;

    Smile(String path, String... tags) {
        this.tags = tags;
        this.path = IMAGES_SMILES_PATH + path + ".gif";
    }

    public String[] getTags() {
        return tags;
    }

    public String getPath() {
        return path;
    }
}
