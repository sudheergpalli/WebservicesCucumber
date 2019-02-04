package com.adp.autopay.automation.tabularParameters;

public class Tabular_FourStrings
{

	private final String Field_1;
	private final String Field_2;
	private final String Field_3;
	private final String Field_4;

	public Tabular_FourStrings(String Field_1, String Field_2,String Field_3,String Field_4)
	{
		super();
		this.Field_1 = Field_1;
		this.Field_2 = Field_2;
		this.Field_3 = Field_3;
		this.Field_4 = Field_4;
	}
	
	public String Field_1()
	{
		return Field_1;
	}

	public String Field_2()
	{
		return Field_2;
	}

	public String Field_3()
	{
		return Field_3;
	}
	public String Field_4()
	{
		return Field_4;
	}
}
