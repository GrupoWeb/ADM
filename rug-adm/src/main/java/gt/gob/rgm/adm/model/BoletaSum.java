package gt.gob.rgm.adm.model;

import java.math.BigDecimal;

public class BoletaSum {
	private String tipoPago;
	private BigDecimal monto;
	private Integer usada;

	public BoletaSum(String tipoPago, Integer usada, BigDecimal monto) {
		super();
		this.tipoPago = tipoPago;
		this.monto = monto;
		this.usada = usada;
	}
	
	public String getTipoPago() {
		return tipoPago;
	}
	
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	
	public BigDecimal getMonto() {
		return monto;
	}
	
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Integer getUsada() {
		return usada;
	}

	public void setUsada(Integer usada) {
		this.usada = usada;
	}
}
