'use client';

import { useEffect, useState } from "react";

export default function Create() {

  const [member, setMember] = useState({id:"",username:"",password:"",email:""});

  const getMe = async () => {
    await fetch("http://localhost:8010/api/v1/members/me",{
      method:"GET",
      credentials: 'include'
    }).then(res => res.json())
    .then(res => {
      setMember(res.data.memberDto);
    });
  }

  useEffect( () => {
    getMe();
  },[]);


  return (
    <main className="min-h-screen p-24">
      <div>
        id : {member.id}
      </div>
      <div>
        name : {member.username}
      </div>
      <div>
        email : {member.email}
      </div>
    </main>
  );
}
