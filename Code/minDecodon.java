public class minDecodon
{
	int num;
	String example;

	public minDecodon(int n, String e)
	{
		num = n;
		example = e;
	}

	public int getNum()
	{
		return num;
	}

	public String getExample()
	{
		return example;
	}

	public String toString()
	{
		return num + " " + example;
	}
}