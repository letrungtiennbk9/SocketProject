//Các thư viện liên quan đến việc đọc ghi files
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//Các thư viện liên quan đến việc đọc, ghi đối tượng bất kỳ
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//Các thư viện chuẩn của các kiểu dữ liệu cơ bản
import java.util.ArrayList;
import java.util.HashMap;
//Các thư viện liên quan đến việc xử lý socket
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;





/*
Proxy này sẽ tạo một ServerSocket và nó sẽ có trách nhiệm đợi những kết nối đến ở từng port nhất định 
Một khi có kết nối đến và socket chấp thuận kết nối này, Proxy sẽ tạo một đối tượng tên là XuLyYeuCau ở một theard
mới và truyền socket vào trong nó để xử lý. Vì ta tạo luồng mới cho nên Proxy có khả năng nhận thêm nhiều kết nối
khác trong khi đã có nhiều kết nối đến trước đó.

Proxy còn có khả năng cho phép chúng ta quản lý, chỉnh sửa thông qua cửa sổ console, nói cách khác, Proxt có khả
năng cho phép quản trị viên block websites theo kiểu thời gian thực mà không cần phải reset server vì chức năng
này hoạt động trên một theard riêng biệt, không làm gián đoạn công việc block, cache của Proxy.

Proxy còn có chức năng duy trì bộ nhớ đệm của các websites được yêu cầu, bao gồm HTML markup, hình ảnh, css, js,
các file đi cùng với mỗi trang web.

Mỗi khi server đóng, tất cả dữ liệu về những trang bị block hoặc những trang cần cached đều được ghi lại xuống file
điều này giúp Proxy có thể luôn duy trì, cập nhật và lưu trữ chúng.
*/
public class Proxy implements Runnable {    //Tạo class Runnable vì Proxy là đối tượng được thực thi trên Thread
	// Phương pháp chính cho chương trình
	public static void main(String[] args) {
	// Tạo một Proxy mẫu và bắt đầu lắng nghe những yêu cầu được kết nối đến.
	Proxy myProxy = new Proxy(8888);
	myProxy.listen();	
    	}
    
   	private ServerSocket serverSocket; // Server Socket

    	/**
     	* Đặt cờ để báo hiệu cho hệ thống quản lý Proxy và Consolee
     	**/
    	private volatile boolean dangChay = true; // đang chạy


	/**
	* Cấu trúc dữ liệu cho việc tìm kiếm tuần tự liên tục của các mục nhớ cache.
	* Khóa: URL của trang/hình ảnh được yêu cầu.
	* Giá trị: File trong bộ nhớ liên kết với Khóa.
	**/
	static HashMap<String, File> boNhoCache; // bộ nhớ cache

	/**
	* Cấu trúc dữ liệu cho việc tìm kiếm tuần tự liên tục của các trang web Bị Chặn.
	* Khóa: URL của trang/hình ảnh được yêu cầu.
	* Giá trị: URL của trang/hình ảnh được yêu cầu.
	**/
	static HashMap<String, String> trangBiChan; // trang bị chặn

	/**
	* Mảng danh sách của các luồng đang chạy và xử lý yêu cầu.
	* Danh sách này nhầm mục đích nối tất cả các luồng trong việc đóng Server.
	**/
	static ArrayList<Thread> luongDangChay; // luồng đang chạy
}
