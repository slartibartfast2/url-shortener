package ea.slartibartfast2.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class UrlConverter {

    private static final String ALLOWED_CHARS_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final char[] ALLOWED_CHARACTERS = ALLOWED_CHARS_STR.toCharArray();
    private static final int ALLOWED_CHARS_LENGTH = ALLOWED_CHARACTERS.length;

    public String encode(long input){
        var encodedString = new StringBuilder();

        if(input == 0) {
            return String.valueOf(ALLOWED_CHARACTERS[0]);
        }

        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS[(int) (input % ALLOWED_CHARS_LENGTH)]);
            input = input / ALLOWED_CHARS_LENGTH;
        }

        return encodedString.reverse().toString();
    }

    public long decodeUrlId(String input) {
        var characters = input.toCharArray();
        var length = characters.length;

        var decoded = 0;

        //counter is used to avoid reversing input string
        var counter = 1;
        for (char character : characters) {
            decoded += ALLOWED_CHARS_STR.indexOf(character) * Math.pow(ALLOWED_CHARS_LENGTH, length - counter);
            counter++;
        }
        return decoded;
    }
}
