package com.cnsunrun.common.util;

import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.List;

import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

/**
 * Java汉字转换为拼音
 */
public class IMChatRecordUtils {





    public static SpannableStringBuilder getColoredDisplayName(String filterStr, String displayName) {
        return getColored(filterStr, displayName);
    }


    public static SpannableStringBuilder getColoredName(String filterStr, String name) {
        return getColored(filterStr, name);
    }

    public static SpannableStringBuilder getColored(String filterStr, String name) {
        try {
            String lowerCaseFilterStr = filterStr.toLowerCase();
            String lowerCaseName = name.toLowerCase();
            String lowerCaseNameSpelling = CharacterParser.getInstance().getSelling(name).toLowerCase();
            if (lowerCaseName.contains(lowerCaseFilterStr)) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                int index = lowerCaseName.indexOf(lowerCaseFilterStr);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, index + filterStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                return spannableStringBuilder;
            } else if (lowerCaseNameSpelling.startsWith(lowerCaseFilterStr)) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                int nameLength = name.length();
                int showCount = 1;
                for (int i = 0; i < nameLength; i++) {
                    String subName = name.substring(0, i + 1);
                    if (filterStr.length() > CharacterParser.getInstance().getSelling(subName).length()) {
                        showCount++;
                        continue;
                    } else {
                        break;
                    }
                }
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), 0, showCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                return spannableStringBuilder;
            } else {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                return spannableStringBuilder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SpannableStringBuilder(name);
    }

    public static SpannableStringBuilder getColoredGroupName(String filterStr, String groupName) {
        return getColored(filterStr, groupName);
    }


    public static SpannableStringBuilder getColoredChattingRecord(String filterStr, MessageContent messageContent) {
        SpannableStringBuilder messageText = new SpannableStringBuilder();
        if (messageContent instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) messageContent;
            String textMessageContent = textMessage.getContent();
            messageText = getOmitColored(filterStr, textMessageContent, 0);
        }
        if (messageContent instanceof RichContentMessage) {
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            String messageTitle = richContentMessage.getTitle();
            messageText = getOmitColored(filterStr, messageTitle, 1);
            if (messageText.length() == 0) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("[链接] ");
                spannableStringBuilder.append(messageTitle);
                messageText = spannableStringBuilder;
            }
        }
        if (messageContent instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) messageContent;
            String fileName = fileMessage.getName();
            messageText = getOmitColored(filterStr, fileName, 2);
        }
        return messageText;
    }

    private static SpannableStringBuilder getOmitColored(String filterStr, String content, int type) {
        SpannableStringBuilder messageText = new SpannableStringBuilder();
        String lowerCaseFilterStr = filterStr.toLowerCase();
        String lowerCaseText = content.toLowerCase();
        if (lowerCaseText.contains(lowerCaseFilterStr)) {
            SpannableStringBuilder finalBuilder = new SpannableStringBuilder();
            if (type == 0) {
            } else if (type == 1) {
                finalBuilder.append("[链接] ");
            } else if (type == 2) {
                finalBuilder.append("[文件] ");
            }
            int length = content.length();
            int firstIndex = lowerCaseText.indexOf(lowerCaseFilterStr);
            String subString = content.substring(firstIndex);
            int restLength;
            if (subString != null) {
                restLength = subString.length();
            } else {
                restLength = 0;
            }
            if (length <= 12) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), firstIndex, firstIndex + filterStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                return finalBuilder.append(spannableStringBuilder);

            } else {
                //首次出现搜索字符的index加上filter的length；
                int totalLength = firstIndex + filterStr.length();
                if (totalLength < 12) {
                    String smallerString = content.substring(0, 12);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), firstIndex, firstIndex + filterStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.append("...");
                    return finalBuilder.append(spannableStringBuilder);
                } else if (restLength < 12) {
                    String smallerString = content.substring(length - 12, length);
                    String smallerStringLowerCase = lowerCaseText.substring(length - 12, length);
                    int index = smallerStringLowerCase.indexOf(lowerCaseFilterStr);
                    SpannableStringBuilder builder = new SpannableStringBuilder("...");
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, index + filterStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    builder.append(spannableStringBuilder);
                    return finalBuilder.append(builder);
                } else {
                    int beginIndex = Math.max(firstIndex - 5, 0);
                    int endIndex = Math.min(firstIndex + 7, content.length());
                    String smallerString = content.substring(beginIndex, endIndex);
                    String smallerStringLowerCase = lowerCaseText.substring(beginIndex, endIndex);
                    int index = smallerStringLowerCase.indexOf(lowerCaseFilterStr);
                    index=Math.max(index,0);
                    SpannableStringBuilder builder = new SpannableStringBuilder("...");
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, getSmallerLength(smallerString.length(), index + filterStr.length()), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    builder.append(spannableStringBuilder);
                    builder.append("...");
                    return finalBuilder.append(builder);
                }
            }
        }
        return messageText;
    }


    private static int getSmallerLength(int stringLength, int endIndex) {
        return stringLength > endIndex + 1 ? endIndex : stringLength;
    }
}
