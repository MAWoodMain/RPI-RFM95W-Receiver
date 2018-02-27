package me.mawood.rfm95w.radio;

public class RFM95W_Registers
{
    static final byte REG_FIFO                      = 0x00;
    static final byte REG_FIFO_ADDR_PTR             = 0x0D;
    static final byte REG_FIFO_TX_BASE_AD           = 0x0E;
    static final byte REG_FIFO_RX_BASE_AD           = 0x0F;
    static final byte REG_RX_NB_BYTES               = 0x13;
    static final byte REG_OPMODE                    = 0x01;
    static final byte REG_FIFO_RX_CURRENT_ADDR      = 0x10;
    static final byte REG_IRQ_FLAGS                 = 0x12;
    static final byte REG_DIO_MAPPING_1             = 0x40;
    static final byte REG_DIO_MAPPING_2             = 0x41;
    static final byte REG_MODEM_CONFIG              = 0x1D;
    static final byte REG_MODEM_CONFIG2             = 0x1E;
    static final byte REG_MODEM_CONFIG3             = 0x26;
    static final byte REG_LR_PARAMP                 = 0x0A;
    static final byte REG_SYMB_TIMEOUT_LSB          = 0x1F;
    static final byte REG_PKT_SNR_VALUE	            = 0x19;
    static final byte REG_PAYLOAD_LENGTH            = 0x22;
    static final byte REG_IRQ_FLAGS_MASK            = 0x11;
    static final byte REG_MAX_PAYLOAD_LENGTH        = 0x23;
    static final byte REG_HOP_PERIOD                = 0x24;
    static final byte REG_SYNC_WORD	                = 0x39;
    static final byte REG_VERSION                   = 0x42;
    static final byte REG_LR_PACONFIG               = 0x09;
    static final byte REG_LR_PKTRSSIVALUE           = 0x1A;


    static final byte REG_FRF_MSB                   = 0x06;
    static final byte REG_FRF_MID                   = 0x07;
    static final byte REG_FRF_LSB                   = 0x08;

    static final byte REG_LNA                       = 0x0C;
    static final byte LNA_MAX_GAIN                  = 0x23;
    static final byte LNA_OFF_GAIN                  = 0x00;
    static final byte LNA_LOW_GAIN                  = 0x20;


    static final byte PA_MAX_BOOST                  = (byte)0x8F;
    static final byte PA_LOW_BOOST                  = (byte)0x81;
    static final byte PA_MED_BOOST                  = (byte)0x8A;
    static final byte PA_OFF_BOOST                  = 0x00;

}
