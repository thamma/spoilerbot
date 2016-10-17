import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpoilerBot extends TelegramLongPollingBot {

    private String botToken;

    public SpoilerBot(String token) {
        this();
        this.botToken = token;
    }

    public SpoilerBot() {

    }

    @Override
    public String getBotToken() {
        if (this.botToken == null) {
            System.out.println("Please provide a bot token:");
            this.botToken = new Scanner(System.in).nextLine();
        }
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            System.out.println(update.getCallbackQuery().getData());

            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setText("FOOBAR");
            answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            answerCallbackQuery.setShowAlert(false);
            try {
                answerCallbackQuery(answerCallbackQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        try {
            if (update.hasInlineQuery()) {
                handleIncomingInlineQuery(update.getInlineQuery());
            } else {
                handleMessage(update);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SpoilerBot";
    }

    private void handleMessage(Update update) throws InvalidObjectException {
        Message message = update.getMessage();
        String input = message.getText();
        System.out.printf("update received: %s", input);
        if (update.hasInlineQuery()) {
            handleIncomingInlineQuery(update.getInlineQuery());
        } else if (message.getText().equalsIgnoreCase("/start")) {
            sendMessage("Welcome!", message);
            try {
                sendMessage(newSendMessage(message.getChatId().toString()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private void handleIncomingInlineQuery(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();
        System.out.println(query);

        AnswerInlineQuery out = new AnswerInlineQuery();
        out.setCacheTime(1000);
        out.setInlineQueryId(inlineQuery.getId());
        List<InlineQueryResult> list = new ArrayList<>();

        InlineQueryResultArticle inlineQueryResult = new InlineQueryResultArticle();
        inlineQueryResult.setReplyMarkup(getKeyboard());
        inlineQueryResult.setDescription("descr");
        inlineQueryResult.setId(Integer.toString(2));
        inlineQueryResult.setTitle("title");
        inlineQueryResult.setUrl("http://vignette4.wikia.nocookie.net/fallout/images/c/c3/Fallout3e.jpg");

        //out.set

        out.setResults(list);

        try {
            answerInlineQuery(out);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, Message target) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setText(message);
        sendMessageRequest.setChatId(target.getChatId().toString());
        try {
            sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static void main(String... args) {
        SpoilerBot spoilerBot;
        if (args.length > 0)
            spoilerBot = new SpoilerBot(args[0]);
        else
            spoilerBot = new SpoilerBot();
        SpoilerBot finalSpoilerBot = spoilerBot;
        new Thread(() -> {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                System.out.println("starting");
                telegramBotsApi.registerBot(finalSpoilerBot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static SendMessage newSendMessage(String id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setReplyMarkup(getKeyboard());
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(id);
        sendMessage.setText("SAMPLE TEXT");

        return sendMessage;
    }

    private static InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Button 1");
        button1.setCallbackData("button");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Button 2");
        button2.setCallbackData("foobar");
        row1.add(button1);
        row1.add(button2);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Button 3");
        button3.setCallbackData("foobar");
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Button 4");
        button4.setCallbackData("foobar");
        row1.add(button3);
        row1.add(button4);

        buttons.add(row1);
        buttons.add(row2);

        inlineKeyboardMarkup.setKeyboard(buttons);

        return inlineKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getSettingsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("1");
        keyboardFirstRow.add("2");
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("3");
        keyboardSecondRow.add("4");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}















