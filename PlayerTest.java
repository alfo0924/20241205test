package demo.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p01;

    @BeforeEach
    void setUp() {
        p01 = new Player();
    }

    @Test
    void weakTest() {
        int count = 0;
        do {
            p01.move();
            count++;
        } while (count < 36);
        assertEquals(28, p01.getEnergy());
        assertTrue(p01.getStatus().equals("虛弱"));
    }

    @Test
    void supermanTest() {
        // 測試吃仙丹後進入超人狀態
        for (int i = 0; i < 2; i++) {
            p01.eatElixir();
        }
        assertEquals(120, p01.getEnergy());
        assertTrue(p01.getStatus().equals("超人"));

        // 測試超人狀態下移動只消耗1能量
        p01.move();
        assertEquals(119, p01.getEnergy());
    }

    @Test
    void normalStateTest() {
        // 測試初始狀態
        assertEquals(100, p01.getEnergy());
        assertTrue(p01.getStatus().equals("正常"));

        // 測試正常狀態下的移動消耗
        p01.move();
        assertEquals(98, p01.getEnergy());
    }

    @Test
    void punchTest() {
        // 測試正常狀態下出拳
        p01.punch();
        assertEquals(95, p01.getEnergy());

        // 測試虛弱狀態下出拳
        while (p01.getEnergy() >= 30) {
            p01.takeHit();
        }
        int energyBeforePunch = p01.getEnergy();
        p01.punch();
        assertEquals((int)(energyBeforePunch - 5 * 1.2), p01.getEnergy());
    }

    @Test
    void takeHitTest() {
        p01.takeHit();
        assertEquals(80, p01.getEnergy());
    }

    @Test
    void energyBoundaryTest() {
        // 測試能量不會低於0
        while (p01.getEnergy() > 0) {
            p01.takeHit();
        }
        assertEquals(0, p01.getEnergy());
        p01.takeHit(); // 再次受到攻擊
        assertEquals(0, p01.getEnergy());
    }

    @Test
    void stateTransitionTest() {
        // 測試狀態轉換：正常 -> 虛弱 -> 正常 -> 超人
        assertTrue(p01.getStatus().equals("正常"));

        // 轉換到虛弱狀態
        while (p01.getEnergy() >= 30) {
            p01.takeHit();
        }
        assertTrue(p01.getStatus().equals("虛弱"));

        // 恢復到正常狀態
        while (p01.getEnergy() < 30) {
            p01.eatElixir();
        }
        assertTrue(p01.getStatus().equals("正常"));

        // 轉換到超人狀態
        while (p01.getEnergy() <= 100) {
            p01.eatElixir();
        }
        assertTrue(p01.getStatus().equals("超人"));
    }
}
