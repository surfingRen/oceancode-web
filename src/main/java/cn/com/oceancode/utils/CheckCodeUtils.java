package cn.com.oceancode.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CheckCodeUtils {

	private static String codeList = "123456789";
	// private static String codeList =
	// "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn" + /*o*/
	// "pqrstuvwxyz1234567890";
	/**
	 * 验证码长度
	 */
	public static int CHECK_CODE_LENGTH = 4;

	private static Color getRandColor(int fc, int bc, Random random) {

		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		if (null == random)
			random = new Random(System.currentTimeMillis());

		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	private static String getRandString(int length) {
		// 生成随机类
		Random random = new Random(System.currentTimeMillis());
		String sRand = "";
		for (int i = 0; i < length; i++) {
			int a = random.nextInt(codeList.length() - 1);
			sRand += codeList.substring(a, a + 1);
		}
		return sRand;
	}

	/**
	 * 取随机产生的认证码(4位数字)
	 * 
	 * @return
	 * 
	 */
	public static String getCheckCode() {
		return getRandString(CHECK_CODE_LENGTH);
	}

	/**
	 * 获取一个验证码图片
	 * 
	 * @param randCode
	 *            验证码
	 * @return
	 * 
	 */
	public static BufferedImage getCheckCodeImage(String randCode) {
		// 生成随机类
		Random random = new Random(System.currentTimeMillis());
		// 在内存中创建图象
		int width = 62, height = 25;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(getRandColor(200, 250, random));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 22));

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		for (int i = 0; i < 155; i++) {
			g.setColor(getRandColor(160, 200, random));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 将认证码显示到图象中
		char[] randCodeArr = randCode.toCharArray();
		for (int i = 0; i < randCodeArr.length; i++) {
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			// g.setColor(new Color(20+random.nextInt(110),
			// 20+random.nextInt(110),
			// 20+random.nextInt(110)));
			g.setColor(getRandColor(60, 150, random));
			g.drawString(randCodeArr[i] + "", 13 * i + 6, 16);
		}

		// 图象生效
		g.dispose();

		return image;
	}// getRandCodeImage

}
