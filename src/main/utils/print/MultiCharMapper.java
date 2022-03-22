package main.utils.print;

public class MultiCharMapper implements CharMapper {
    private CharMapper[] mappers;

    public MultiCharMapper() {
        this.mappers = null;
    }

    public MultiCharMapper(CharMapper... mappers) {
        this.mappers = mappers;
    }

    public void addCharMapper(CharMapper mapper) {
        if (this.mappers == null) {
            this.mappers = new CharMapper[]{mapper};
        } else {
            int n = this.mappers.length;
            CharMapper[] newArray = new CharMapper[n + 1];
            System.arraycopy(this.mappers, 0, newArray, 0, n);
            newArray[n] = mapper;
            this.mappers = newArray;
        }

    }

    public String map(int codePoint) {
        if (this.mappers != null) {
            CharMapper[] var2 = this.mappers;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                CharMapper mapper = var2[var4];
                String result = mapper.map(codePoint);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }
}
