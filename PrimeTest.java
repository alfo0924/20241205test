package demo.prime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrimeTest {

    /**
     * 沒有使用 Mockito，使用自定義的 Mock 物件
     */
    @Test
    public void testAllPrimeWithMyMock() {
        PrimeChecker realCheck = new MockPrimeChecker();
        Prime prime = new Prime(realCheck);
        int[] result = prime.allPrime(5);
        assertArrayEquals(new int[]{2, 3, 5}, result);
    }

    /**
     * 使用 Mockito 測試基本質數檢查功能
     */
    @Test
    public void testAllPrimeWithMockedIsPrime() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);

        when(mockChecker.isPrime(2)).thenReturn(true);
        when(mockChecker.isPrime(3)).thenReturn(true);
        when(mockChecker.isPrime(4)).thenReturn(false);
        when(mockChecker.isPrime(5)).thenReturn(true);
        when(mockChecker.isPrime(7)).thenReturn(true);

        Prime prime = new Prime(mockChecker);
        int[] result = prime.allPrime(10);
        assertArrayEquals(new int[]{2, 3, 5, 7}, result);

        verify(mockChecker, times(1)).isPrime(2);
        verify(mockChecker, times(1)).isPrime(3);
        verify(mockChecker, times(1)).isPrime(4);
        verify(mockChecker, times(1)).isPrime(5);
    }

    /**
     * 測試邊界值情況
     */
    @Test
    public void testBoundaryValues() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        
        // 測試邊界值 0 和 1
        when(mockChecker.isPrime(0)).thenReturn(false);
        when(mockChecker.isPrime(1)).thenReturn(false);
        when(mockChecker.isPrime(2)).thenReturn(true);
        
        Prime prime = new Prime(mockChecker);
        int[] result = prime.allPrime(2);
        assertArrayEquals(new int[]{2}, result);
        
        verify(mockChecker, never()).isPrime(-1); // 確認負數未被檢查
        verify(mockChecker, times(1)).isPrime(2);
    }

    /**
     * 測試大範圍質數檢查
     */
    @Test
    public void testLargeNumbers() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        
        // 模擬較大數字的質數檢查
        when(mockChecker.isPrime(97)).thenReturn(true);
        when(mockChecker.isPrime(98)).thenReturn(false);
        when(mockChecker.isPrime(99)).thenReturn(false);
        when(mockChecker.isPrime(100)).thenReturn(false);
        
        Prime prime = new Prime(mockChecker);
        int[] result = prime.allPrime(100);
        assertTrue(contains(result, 97));
        assertFalse(contains(result, 98));
    }

    /**
     * 測試連續的非質數情況
     */
    @Test
    public void testConsecutiveNonPrimes() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        
        // 模擬連續的非質數
        when(mockChecker.isPrime(24)).thenReturn(false);
        when(mockChecker.isPrime(25)).thenReturn(false);
        when(mockChecker.isPrime(26)).thenReturn(false);
        when(mockChecker.isPrime(27)).thenReturn(false);
        when(mockChecker.isPrime(28)).thenReturn(false);
        
        Prime prime = new Prime(mockChecker);
        int[] result = prime.allPrime(28);
        
        // 驗證這些數字都不在結果中
        assertFalse(contains(result, 24));
        assertFalse(contains(result, 25));
        assertFalse(contains(result, 26));
        assertFalse(contains(result, 27));
        assertFalse(contains(result, 28));
    }

    /**
     * 測試空結果的情況
     */
    @Test
    public void testEmptyResult() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        
        // 模擬所有數字都不是質數的情況
        for(int i = 0; i <= 5; i++) {
            when(mockChecker.isPrime(i)).thenReturn(false);
        }
        
        Prime prime = new Prime(mockChecker);
        int[] result = prime.allPrime(1);
        assertEquals(0, result.length);
    }

    /**
     * 測試異常處理 - 範圍超過限制
     */
    @Test
    public void testRangeExceedException() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        when(mockChecker.isPrime(101)).thenThrow(new IllegalArgumentException("超過允許的範圍"));
        
        Prime prime = new Prime(mockChecker);
        assertThrows(IllegalArgumentException.class, () -> prime.allPrime(101));
    }

    /**
     * 測試異常處理 - 負數輸入
     */
    @Test
    public void testNegativeInput() {
        PrimeChecker mockChecker = mock(PrimeChecker.class);
        when(mockChecker.isPrime(-1)).thenThrow(new IllegalArgumentException("不允許負數"));
        
        Prime prime = new Prime(mockChecker);
        assertThrows(IllegalArgumentException.class, () -> prime.allPrime(-1));
    }

    // 輔助方法：檢查陣列是否包含特定值
    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) return true;
        }
        return false;
    }
}
