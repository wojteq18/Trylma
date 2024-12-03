use super::traits::Message;

pub struct move_message
{
    pub from: (u8, u8),
    pub to: (u8, u8),
}

impl Message for move_message
{
    fn get_content(&self) -> String
    {
        return format!("Move from ({}, {}) to ({}, {})", self.from.0, self.from.1, self.to.0, self.to.1);
    }
}