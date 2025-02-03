import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@headlessui/react";
import { ChatRoomType } from "../../types";
import { chatrooms } from "../../api";

export default function Dashboard() {
    const navigate = useNavigate();
    const [chatRooms, setChatRooms] = useState<ChatRoomType[]>([]);

    useEffect(() => {
        fetchChatRooms();
    }, []);

    const fetchChatRooms = async () => {
        const response = await chatrooms();
        setChatRooms(response !== null? response : []);
    };

    const handleClick = (roomId: string) => {
        navigate(`/agent/chat/${roomId}`);
    };

    return ( 
        <div className="flex flex-col items-center min-h-screen bg-gray-900 text-white p-6">
            <h1 className="text-2xl font-bold mb-4">Waiting Rooms</h1>

            {/* Chatrooms */}
            <div className="w-full max-w-2xl bg-white/10 p-6 rounded-lg shadow-md">
                <h2 className="text-xl font-semibold mb-4">Chat Rooms</h2>

                {chatRooms.length > 0 ? (
                    <ul className="space-y-3">
                        {chatRooms.map((room) => (
                            <li
                                key={room.roomId}
                                className="flex justify-between items-center p-3 bg-gray-800 rounded-lg"
                            >
                                <span className="text-lg">{room.roomId}</span>
                                <Button
                                    onClick={() => handleClick(room.roomId)}
                                    className="bg-blue-600 px-4 py-2 text-sm font-semibold rounded-md hover:bg-blue-500"
                                >
                                    Join
                                </Button>
                            </li>
                        ))}
                    </ul>
                ) : (
                <p className="text-gray-400">No chat rooms available.</p>
                )}
            </div>
    </div>
    );
};