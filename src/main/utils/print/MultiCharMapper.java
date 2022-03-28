package main.utils.print;

public class MultiCharMapper {
    private CharMapper[] mappers;

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
