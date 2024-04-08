'use client';

import { useState } from "react";

export default function Create() {

  const [member, setMember] = useState({username:"",password:""});

  const handlerClick = async () => {
    await fetch("http://localhost:8010/api/v1/members/login",{
      method:"POST",
      credentials: 'include', // 클라이언트와 서버가 통신할때 쿠키 값을 공유하겠다는 설정
      headers:{'Content-Type':"application/json"},
      body:JSON.stringify(member)
    }).then(res => res.json())
    .then(data => {
      alert(data.msg);
    });

  }

  const handlerChange = (e) => {
    const {name, value} = e.target;
    setMember({...member,[name]:value});
  }

  return (
    <main className="min-h-screen p-24">
      <div>
        <div className="flex justify-center">
          <input onChange={handlerChange} name="username" type="text" placeholder="아이디를 입력하세요" className="input input-bordered mx-auto" />
        </div>
        <div className=" flex justify-center mt-4">
          <input onChange={handlerChange} name="password" type="password" placeholder="비밀번호를 입력하세요" className="input input-bordered" />
        </div>

        <button onClick={handlerClick} className="btn mt-3">로그인</button>
        
      </div>
    </main>
  );
}
