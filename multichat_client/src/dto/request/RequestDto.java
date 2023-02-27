package dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestDto<T> {			// json형태로 변환하는 것이 번거롭기에, 제네릭 사용

	private String resource;
	private T body;				// body의 형태가 string일 수도, 등등 일 수도 있음
}
