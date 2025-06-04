# Ölçüm Analiz Programı

Bu program, sıcaklık ve nem ölçümlerini analiz etmek için geliştirilmiş bir Java uygulamasıdır.

## Özellikler

- Sıcaklık ve nem ölçümlerini ayrı ayrı analiz etme
- Ortalama hesaplama
- Maximum değer bulma
- Minimum değer bulma
- Standart sapma hesaplama
- Frekans analizi
- Medyan hesaplama
- Dosya bazlı ve global hesaplama desteği

## Gereksinimler

- Java 11 veya üzeri
- Swing GUI kütüphanesi (Java SE ile birlikte gelir)

## Kurulum

1. Projeyi bilgisayarınıza klonlayın
2. Java IDE'nizde (Eclipse, IntelliJ IDEA, vb.) projeyi açın
3. Main.java dosyasını çalıştırın

## Kullanım

1. Program başlatıldığında "Klasör Seç" butonuna tıklayın
2. Ölçüm verilerinin bulunduğu klasörü seçin
   - Klasör yapısı şu şekilde olmalıdır:
     ```
     olcumler/
     ├── sicaklik/
     │   └── id1_Sicaklik_YER_TARIH.txt
     └── nem/
         └── id1_Nem_YER_TARIH.txt
     ```
3. Yapmak istediğiniz hesaplamaları checkbox'ları işaretleyerek seçin
4. "Hesapla" butonuna tıklayın
5. Sonuçlar "sonuc" klasöründe oluşturulacaktır

## Dosya Formatı

Ölçüm dosyaları aşağıdaki formatta olmalıdır:

```
id:1 ölçüm: sicaklik - yer: YER - tarih: GG.AA.YYYY
SAAT:DAKİKA:SANİYE,DEĞER
...
```

## Sonuçlar

Hesaplama sonuçları "sonuc" klasöründe, sıcaklık ve nem için ayrı klasörlerde oluşturulur. Her hesaplama türü için ayrı bir dosya oluşturulur. 