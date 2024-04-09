'use client'

import Link from "next/link";

export default function MemberLayout ({
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>) {

    const handleLogout = async () => {
        const response = await fetch("http://localhost:8010/api/v1/members/logout", {
            method: 'POST',
            credentials: 'include', // 핵심 변경점
            headers: {
                'Content-Type': 'application/json' 
            }
        })

        if (response.ok) {
            alert("ok")
        } else {
            alert("fail")
        }
    }

    return (
        <>
            <h1>맴버 페이지 공통요소</h1>
            <nav>
                <ul>
                    <li>
                        <Link href="/">홈</Link>
                    </li>
                    <li>
                        <Link href="/about">소개</Link>
                    </li>
                    <li>
                        <Link href="/article">게시판</Link>
                    </li>
                    <li>
                        <Link href="/member/login">로그인</Link>   
                    </li>
                    <li>
                        <button onClick={handleLogout}>로그아웃</button>
                    </li>
                </ul>
            </nav> 
            {children}
        </>
    )
}