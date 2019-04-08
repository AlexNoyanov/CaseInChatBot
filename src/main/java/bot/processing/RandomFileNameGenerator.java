package bot.processing;

import java.net.StandardSocketOptions;
import java.util.Random;

public class RandomFileNameGenerator {
    private Random random;

    public RandomFileNameGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public String getRandomName() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 20; i++) {
            builder.append((char)(random.nextInt(26) + 'a'));
        }
        return builder.toString();
    }
}
