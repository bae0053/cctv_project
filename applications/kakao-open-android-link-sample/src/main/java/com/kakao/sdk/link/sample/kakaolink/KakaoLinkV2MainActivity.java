package com.kakao.sdk.link.sample.kakaolink;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.CommerceDetailObject;
import com.kakao.message.template.CommerceTemplate;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.ListTemplate;
import com.kakao.message.template.LocationTemplate;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.sdk.link.sample.BuildConfig;
import com.kakao.sdk.link.sample.R;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class KakaoLinkV2MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_link40_main);

        final String[] methods = new String[] {
                getString(R.string.title_default_feed),
                getString(R.string.title_default_list),
                getString(R.string.title_default_location),
                getString(R.string.title_default_commerce),
                getString(R.string.title_default_text),
                getString(R.string.title_scrap),
                getString(R.string.title_custom_feed),
        };

        final int[] images = new int[] {
                R.drawable.icon_feed,
                R.drawable.icon_list,
                R.drawable.icon_location,
                R.drawable.icon_custom,
                R.drawable.icon_custom,
                R.drawable.icon_scrap,
                R.drawable.icon_custom
        };

        ListView listView = findViewById(R.id.link40_method_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return methods.length;
            }

            @Override
            public Object getItem(int position) {
                return methods[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) KakaoLinkV2MainActivity.this
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null && mInflater != null) {
                    convertView = mInflater.inflate(R.layout.menu_item, parent, false);
                    ImageView imageView = convertView.findViewById(R.id.method_image);
                    TextView textView = convertView.findViewById(R.id.method_text);
                    imageView.setImageDrawable(getResources().getDrawable(images[position]));
                    textView.setText(methods[position]);
                }
                return convertView;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    sendDefaultFeedTemplate();
                    break;
                case 1:
                    sendDefaultListTemplate();
                    break;
                case 2:
                    sendDefaultLocationTemplate();
                    break;
                case 3:
                    sendDefaultCommerceTemplate();
                    break;
                case 4:
                    sendDefaultTextTemplate();
                    break;
                case 5:
                    sendScrapMessage();
                    break;
                case 6:
                    sendFeedTemplate();
                    break;
                default:
                    break;
            }
        });

        callback = new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(getApplicationContext(), "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
            }
        };
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        callbackParameters.put("user_id", "1234");
        callbackParameters.put("title", "???????????? ????????? ?????? !@#$%");
        return callbackParameters;
    }
    private void sendFeedTemplate() {
        String templateId;
        switch (BuildConfig.FLAVOR) {
            case "dev":
                templateId = "18578";
                break;
            case "sandbox":
                templateId = "222";
                break;
            case "cbt":
            case "production":
                templateId = "3135";
                break;
            default:
                return;
        }

        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("${title}", "???????????? ????????? ??????");
        templateArgs.put("${description}", "?????? 7~8?????? ???????????? ???????????? ???????????? ???????????? ????????????. ??? ?????? ????????????????????? ?????? ???????????? ???????????? ?????? ????????? ????????? ???????????? ??? ??? ??????.");

        KakaoLinkService.getInstance().sendCustom(this, templateId, templateArgs, serverCallbackArgs, callback);
    }

    private void sendScrapMessage() {
        KakaoLinkService.getInstance().sendScrap(this, "https://developers.kakao.com", serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private void sendDefaultFeedTemplate() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("?????? ?????? ??????",
                "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build())
                .setDescrption("#?????? #?????? #????????? #?????? #????????? #?????????")
                .build())
                .setSocial(SocialObject.newBuilder().setLikeCount(286).setCommentCount(45)
                        .setSharedCount(845).build())
                .addButton(new ButtonObject("????????? ??????", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
                .addButton(new ButtonObject("????????? ??????", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, callback);
    }

    private void sendDefaultListTemplate() {
        ListTemplate params = ListTemplate.newBuilder("WEEKLY MAGAZINE",
                LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build())
                .addContent(ContentObject.newBuilder("????????? ??????, ??????",
                        "http://mud-kage.kakao.co.kr/dn/bDPMIb/btqgeoTRQvd/49BuF1gNo6UXkdbKecx600/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("?????????")
                        .build())
                .addContent(ContentObject.newBuilder("???????????? ???????????? ???????????????",
                        "http://mud-kage.kakao.co.kr/dn/QPeNt/btqgeSfSsCR/0QJIRuWTtkg4cYc57n8H80/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("??????")
                        .build())
                .addContent(ContentObject.newBuilder("????????? ???????????? ?????????????????????",
                        "http://mud-kage.kakao.co.kr/dn/c7MBX4/btqgeRgWhBy/ZMLnndJFAqyUAnqu4sQHS0/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("??????").build())
                .addButton(new ButtonObject("????????? ??????", LinkObject.newBuilder()
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build()))
                .addButton(new ButtonObject("????????? ??????", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }

    private void sendDefaultLocationTemplate() {
        LocationTemplate params = LocationTemplate.newBuilder("????????? ????????? ???????????? 235",
                ContentObject.newBuilder("????????? ???????????? ?????????????????????",
                "http://mud-kage.kakao.co.kr/dn/bSbH9w/btqgegaEDfW/vD9KKV0hEintg6bZT4v4WK/kakaolink40_original.png",
                    LinkObject.newBuilder()
                            .setWebUrl("https://developers.kakao.com")
                            .setMobileWebUrl("https://developers.kakao.com")
                            .build())
                    .setDescrption("?????? ?????? ????????????????????? 1+1").build())
                .setSocial(SocialObject.newBuilder().setLikeCount(286).setCommentCount(45).setSharedCount(845).build())
                .setAddressTitle("????????? ???????????????")
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }

    private void sendDefaultCommerceTemplate() {
        CommerceTemplate params = CommerceTemplate.newBuilder(
                ContentObject.newBuilder("????????? ???????????? ?????????????????????",
                        "http://mud-kage.kakao.co.kr/dn/bSbH9w/btqgegaEDfW/vD9KKV0hEintg6bZT4v4WK/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("?????? ?????? ????????????????????? 1+1").build(),
                CommerceDetailObject.newBuilder(12345).setProductName("????????????????????? 1+1").setDiscountPrice(10000).setDiscountRate(20).build())
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }

    private void sendDefaultTextTemplate() {
        TextTemplate params = TextTemplate.newBuilder(
                "Text",
                LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build()
        )
                .setButtonTitle("This is button")
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();
}
