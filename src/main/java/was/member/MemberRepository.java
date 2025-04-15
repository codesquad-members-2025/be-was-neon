package was.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {
    private static final Map<Long, Member> members = new HashMap();
    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        members.put(member.getId(), member);
        return member;
    }

    public List<Member> findAll() {
        return new ArrayList<>(members.values());
    }

}
