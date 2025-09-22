package latihan;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class MyViewModel {

	private int count;

    public MyViewModel(){
        System.out.println("Jalan Pertama");
        this.count = 100;
    }

	@Init
	public void init() {
        System.out.println("Jalan Kedua");
		count = 105;
	}

	@Command
	@NotifyChange("count")
	public void cmd() {
		++count;
	}

	public int getCount() {
		return count;
	}
}
