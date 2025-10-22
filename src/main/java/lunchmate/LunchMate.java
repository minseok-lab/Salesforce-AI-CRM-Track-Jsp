package lunchmate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class LunchMate {
	// [수정] main 메서드 대신, 결과를 반환하는 새로운 메서드를 만듭니다.
	// JSP에서 이 메서드를 호출할 것입니다.
	public List<List<String>> generateLunchGroups() {

		// 현재 각 조별 편성
		String[] firstGroup = { "박자은", "성원호", "이영호", "전나영" };
		String[] secondGroup = { "강주성", "김규남", "김우연", "서승아", "이상민" };
		String[] thirdGroup = { "김은수", "신혜지", "안태현", "엄희진", "임현섭" };
		String[] fourthGroup = { "강은혜", "고아라", "이건", "전정배" };
		String[] fifthGroup = { "강민형", "김민석", "신유진", "이자영", "이주희" };
		String[] sixthGroup = { "박달", "이슬기", "차현수", "황정민" };

		// 전체 조를 순회하기 위한 배열 선언
		String[][] allGroups = { firstGroup, secondGroup, thirdGroup, fourthGroup, fifthGroup, sixthGroup };

		// 동적으로 크기를 변경할 수 있는 중첩 배열을 사용하기 위해 Arraylist 사용
		List<List<String>> newGroups = new ArrayList<>();

		// 6개의 새로운 조(List<String>)를 미리 생성해서 newGroups에 삽입
		for (int i = 0; i < 6; i++) {
			newGroups.add(new ArrayList<String>());
		}

		Random random = new Random();
		// 새로운 조(0~5)에 배정하기 위한 인덱스 카운터
		int newGroupIndex = 0;

		// 1. 외부 for문: 원본 1조부터 6조까지 순회
		for (int i = 0; i < allGroups.length; i++) {

			// 현재 원본 조
			String[] currentOriginalGroup = allGroups[i];

			// 2. (랜덤화) 현재 원본 조의 멤버 순서를 섞음
			// Arrays.asList로 감싸야 Collections.shuffle을 사용 가능
			Collections.shuffle(Arrays.asList(currentOriginalGroup), random);

			// 3. 내부 for문: 섞인 조의 멤버들을 한 명씩 순회
			for (int j = 0; j < currentOriginalGroup.length; j++) {
				String person = currentOriginalGroup[j];

				// 4. (배분) newGroupIndex (0~5)에 해당하는 새 조에 사람을 추가
				newGroups.get(newGroupIndex).add(person);

				// 5. (인덱스 순환) 다음 사람을 다음 조에 배정하기 위해 인덱스 1 증가
				// 5 다음에는 0이 되어야 하므로 % 6 연산 사용
				newGroupIndex = (newGroupIndex + 1) % 6;
			}
		}

		System.out.println("\n점심 메이트 추첨을 시작합니다!");
		for (int i = 0; i < newGroups.size(); i++) {
			System.out.printf("새로운 %d조 (%d명): %s\n", (i + 1), newGroups.get(i).size(), // 조 인원수
					newGroups.get(i));
		}
		System.out.println("즐거운 점심시간 되세요!");

		return newGroups;
	}
}
