package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        //TODO Task 1.1 - 5 marks
        int startIndex = _htmlCode.indexOf("<title>");
        String title;
        try {
            title = _htmlCode.substring(startIndex);
        }catch (Exception e){
            return "Title not found!";
        }
        int breakIndex = title.indexOf("|");

        title = title.substring(7,breakIndex);

        if(!title.isEmpty()){
            title = title.substring(0,title.length()-1);
        }

        return title;
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        //TODO Task 1.2 - 5 marks
        int startIndex = _htmlCode.indexOf("\"articleBody\": ");
        String content;
        try {
            content = _htmlCode.substring(startIndex);
        }catch (Exception e){
            return "Content not found!";
        }
        int endIndex = content.indexOf("\",\"mainEntityOfPage\":");
        content = content.substring("\"articleBody\": ".length()+1, endIndex-1);
        content = content.toLowerCase();
        return content.trim();
    }


}
