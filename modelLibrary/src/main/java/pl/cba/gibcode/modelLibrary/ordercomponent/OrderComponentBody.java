package pl.cba.gibcode.modelLibrary.ordercomponent;

import io.swagger.annotations.ApiModel;
import pl.cba.gibcode.modelLibrary.model.Body;

/*@ApiModel(value = "OrderComponentBody", description = "Placeholder for concrete OrderComponentBody instances.",
		subTypes = { CreateCustomerBody.class, CreateCryptoPositionBody.class, SendCryptoCurrencyBody.class,
				TransferCryptoCurrencyBody.class, GenerateTempAddressBody.class, ReceiveCryptoCurrencyBody.class,
				EmptyOrderEventBody.class, CloseCustomerBody.class, UpdateClientUserSettingsBody.class, BuyCryptoCurrencyBody.class
})*/
public interface OrderComponentBody extends Body {

}
