package br.com.gestao.commons.deserializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import br.com.gestao.commons.Const;
import br.com.gestao.exceptions.OtherErrorException;


public class DateDeserializer extends JsonDeserializer<Date> {
	// yyyy-MM-dd'T'HH:mm:ss.SSSX
	private SimpleDateFormat formatter = new SimpleDateFormat(Const.FORMATO_DATA_BR);

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		String date = jsonParser.getText();
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new OtherErrorException(e.getMessage(), "Convertendo texto " + date + " para data");
		}
	}
}
