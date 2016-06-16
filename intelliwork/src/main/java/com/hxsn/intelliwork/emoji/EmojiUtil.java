package com.hxsn.intelliwork.emoji;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.hxsn.intelliwork.R;

public class EmojiUtil {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<Emoji>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageID(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }


    public static final int[] EmojiResArray = {
            R.drawable.e1_1,
            R.drawable.e1_2,
            R.drawable.e1_3,
            R.drawable.e1_4,
            R.drawable.e1_5,
            R.drawable.e1_6,
            R.drawable.e1_7,
            R.drawable.e1_8,
            R.drawable.e1_9,
            R.drawable.e1_10,
            R.drawable.e1_11,
            R.drawable.e1_12,
            R.drawable.e1_13,
            R.drawable.e1_14,
            R.drawable.e1_15,
            R.drawable.e1_16,
            R.drawable.e1_17,
            R.drawable.e1_18,
            R.drawable.e1_19,
            R.drawable.e1_20,
            R.drawable.e2_21,
            R.drawable.e2_22,
            R.drawable.e2_23,
            R.drawable.e2_24,
            R.drawable.e2_25,
            R.drawable.e2_26,
            R.drawable.e2_27,
            R.drawable.e2_28,
            R.drawable.e2_29,
            R.drawable.e2_30,
            R.drawable.e2_31,
            R.drawable.e2_32,
            R.drawable.e2_33,
            R.drawable.e2_34,
            R.drawable.e2_35,
            R.drawable.e2_36,
            R.drawable.e2_37,
            R.drawable.e2_38,
            R.drawable.e2_39,
            R.drawable.e2_40,
            R.drawable.e3_41,
            R.drawable.e3_42,
            R.drawable.e3_43,
            R.drawable.e3_44,
            R.drawable.e3_45,
            R.drawable.e3_46,
            R.drawable.e3_47,
            R.drawable.e3_48,
            R.drawable.e3_49,
            R.drawable.e3_50,
            R.drawable.e3_51,
            R.drawable.e3_52,
            R.drawable.e3_53,
            R.drawable.e3_54,
            R.drawable.e3_55,
            R.drawable.e3_56,
            R.drawable.e3_57,
            R.drawable.e3_58,
            R.drawable.e3_59,
            R.drawable.e3_60,
            R.drawable.e4_61,
            R.drawable.e4_62,
            R.drawable.e4_63,
            R.drawable.e4_64,
            R.drawable.e4_65,
            R.drawable.e4_66,
            R.drawable.e4_67,
            R.drawable.e4_68,
            R.drawable.e4_69,
            R.drawable.e4_70,
            R.drawable.e4_71,
            R.drawable.e4_72,
            R.drawable.e4_73,
            R.drawable.e4_74,
            R.drawable.e4_75,
            R.drawable.e4_76,
            R.drawable.e4_77,
            R.drawable.e4_78,
            R.drawable.e4_79,
            R.drawable.e4_80,
            R.drawable.e5_81,
            R.drawable.e5_82,
            R.drawable.e5_83,
            R.drawable.e5_84,
            R.drawable.e5_85,
            R.drawable.e5_86,
            R.drawable.e5_87,
            R.drawable.e5_88,
            R.drawable.e5_89,
            R.drawable.e5_90
    };

    public static final String[] EmojiTextArray = {
            "[01]",
            "[02]",
            "[03]",
            "[04]",
            "[05]",
            "[06]",
            "[07]",
            "[08]",
            "[09]",
            "[10]",
            "[11]",
            "[12]",
            "[13]",
            "[14]",
            "[15]",
            "[16]",
            "[17]",
            "[18]",
            "[19]",
            "[20]",
            "[21]",
            "[22]",
            "[23]",
            "[24]",
            "[25]",
            "[26]",
            "[27]",
            "[28]",
            "[29]",
            "[30]",
            "[31]",
            "[32]",
            "[33]",
            "[34]",
            "[35]",
            "[36]",
            "[37]",
            "[38]",
            "[39]",
            "[40]",
            "[41]",
            "[42]",
            "[43]",
            "[44]",
            "[45]",
            "[46]",
            "[47]",
            "[48]",
            "[49]",
            "[50]",
            "[51]",
            "[52]",
            "[53]",
            "[54]",
            "[55]",
            "[56]",
            "[57]",
            "[58]",
            "[59]",
            "[60]",
            "[61]",
            "[62]",
            "[63]",
            "[64]",
            "[65]",
            "[66]",
            "[67]",
            "[68]",
            "[69]",
            "[70]",
            "[71]",
            "[72]",
            "[73]",
            "[74]",
            "[75]",
            "[76]",
            "[77]",
            "[78]",
            "[79]",
            "[80]",
            "[81]",
            "[82]",
            "[83]",
            "[84]",
            "[85]",
            "[86]",
            "[87]",
            "[88]",
            "[89]",
            "[90]"
    };

    static {
        emojiList = generateEmojis();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // ԴͼƬ�ĸ߶ȺͿ��
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // �����ʵ�ʿ�ߺ�Ŀ���ߵı���
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // ѡ���͸�����С�ı�����ΪinSampleSize��ֵ��������Ա�֤����ͼƬ�Ŀ�͸�
            // һ��������ڵ���Ŀ��Ŀ�͸ߡ�
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // �������涨��ķ�������inSampleSizeֵ
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    //���ݽ���������Ҫ��ʾemoji��TextView����
    public static void handlerEmojiText(TextView comment, String content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //ת��ΪSpan������Span�Ĵ�С
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageID()
                                    , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
    }
}