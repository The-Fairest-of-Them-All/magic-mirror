
package calenderinfo;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author 2
 */
public class CalenderInfo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Calendar calendar = Calendar.getInstance();
       Date date = calendar.getTime();
        System.out.println(date.toString());
       
    }
    
}
