package com.example.busfinder;


import android.os.AsyncTask;


public class CalculateTime extends AsyncTask<String, String, String> {


    public CalculateTime() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        String textResult = "";
        String result = "";


        String latS = params[0];
        String longS = params[1];

        String latD = params[2];
        String longD = params[3];


        String url =
                "https://www.google.com/maps/dir/" + latS + ",+" + longS + "/" + latD + ",+" + longD + "/@32.3244815,34.854499,17z/data=!3m1!4b1!4m10!4m9!1m3!2m2!1d34.8568182!2d32.3247562!1m3!2m2!1d34.8565802!2d32.3243541!3e0?hl=en\n";

        try {
            textResult = SocketConnection
                    .getURLSource(url);



            //decode the info from the text. serching for the text  min/"  and check if it is numeric. if so - it is the arrival time
            for (int j = 0; j < 1; j++) {


                char slesh = (char) 92;
                char quotes = '"';
                String bb = "min" + slesh + quotes;

                int end = textResult.indexOf(bb) + bb.length();
                int i = end - 2;
                while (textResult.charAt(i) != quotes) {
                    i--;
                }
                int start = i;

                result = textResult.substring(start + 1, end - 2);

                if (result.indexOf(" ") == -1 || isNumeric(result.split(" ")[0]) == false)
                    j--;


                textResult = textResult.substring(end, textResult.length());

            }

        } catch (Exception e) {
            System.out.println(e);


        }


        return result;
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
